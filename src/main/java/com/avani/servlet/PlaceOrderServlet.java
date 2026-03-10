package com.avani.servlet;

import com.avani.util.DatabaseConfig;
import com.avani.util.EmailUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/placeOrder")
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // 1️⃣ Read customer details
        String fullName = request.getParameter("fullName");
        String address  = request.getParameter("address");
        String city     = request.getParameter("city");
        String state    = request.getParameter("state");
        String pincode  = request.getParameter("pincode");

        // 2️⃣ Server-side validation
        if (fullName == null || fullName.trim().isEmpty()
                || address == null || address.trim().isEmpty()
                || city == null || city.trim().isEmpty()
                || state == null || state.trim().isEmpty()
                || pincode == null || !pincode.matches("\\d{6}")) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid customer details");
            return;
        }

        Connection con = null;

        try {
            // 3️⃣ Get DB connection via DatabaseConfig
            con = DatabaseConfig.getConnection();
            con.setAutoCommit(false);

            // 4️⃣ Insert customer
            String customerSql =
                    "INSERT INTO customer_details " +
                    "(customer_id, full_name, address, city, state, pin_code) " +
                    "VALUES (" + DatabaseConfig.getNextValSyntax("customer_seq") + ", ?, ?, ?, ?, ?)";

            PreparedStatement psCustomer = con.prepareStatement(customerSql);
            psCustomer.setString(1, fullName);
            psCustomer.setString(2, address);
            psCustomer.setString(3, city);
            psCustomer.setString(4, state);
            psCustomer.setString(5, pincode);
            psCustomer.executeUpdate();

            // 5️⃣ Get generated customer_id
            int customerId = 0;
            String currValQuery = DatabaseConfig.getCurrentValQuery("customer_seq");
            try (PreparedStatement ps = con.prepareStatement(currValQuery);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) customerId = rs.getInt(1);
            }

            String sessionId = request.getSession().getId();

            // 6️⃣ Read cart
            String cartSql =
                    "SELECT product_id, quantity_kg, total_amount " +
                    "FROM cart WHERE session_id = ?";

            PreparedStatement psCart = con.prepareStatement(cartSql);
            psCart.setString(1, sessionId);
            ResultSet rsCart = psCart.executeQuery();

            if (!rsCart.next()) {
                con.rollback();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart is empty");
                return;
            }

            // 7️⃣ Insert orders
            String orderSql =
                    "INSERT INTO orders " +
                    "(order_id, customer_id, product_id, quantity_kg, total_amount) " +
                    "VALUES (" + DatabaseConfig.getNextValSyntax("order_seq") + ", ?, ?, ?, ?)";

            PreparedStatement psOrder = con.prepareStatement(orderSql);

            do {
                int productId = rsCart.getInt("product_id");
                double quantityKg = rsCart.getDouble("quantity_kg");
                double totalAmount = rsCart.getDouble("total_amount");

                psOrder.setInt(1, customerId);
                psOrder.setInt(2, productId);
                psOrder.setDouble(3, quantityKg);
                psOrder.setDouble(4, totalAmount);
                psOrder.executeUpdate();
            } while (rsCart.next());

            // 8️⃣ Clear cart
            PreparedStatement psClearCart =
                    con.prepareStatement("DELETE FROM cart WHERE session_id = ?");
            psClearCart.setString(1, sessionId);
            psClearCart.executeUpdate();

            // 9️⃣ Commit
            con.commit();

            // 📧 Send order email (non-blocking)
            try {
                String emailBody =
                        "New Order Received - Avani Turmerics\n\n" +
                        "Customer Details:\n" +
                        "Name: " + fullName + "\n" +
                        "Address: " + address + "\n" +
                        "City: " + city + "\n" +
                        "State: " + state + "\n" +
                        "Pincode: " + pincode + "\n\n" +
                        "Session ID: " + sessionId;

                EmailUtil.sendOrderEmail("New Order - Avani Turmerics", emailBody);
            } catch (Exception emailEx) {
                emailEx.printStackTrace(); // Don't fail order if email fails
            }

            // 🔟 Return JSON success for cross-origin frontend
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"status\":\"success\",\"message\":\"Order placed successfully\"}");

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ignored) {}
            throw new ServletException(e);

        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception ignored) {}
        }
    }
}
