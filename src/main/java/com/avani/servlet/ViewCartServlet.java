package com.avani.servlet;

import com.avani.util.DatabaseConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/viewCart")
public class ViewCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String sql =
            "SELECT p.product_name, c.quantity_kg, c.price_per_kg, c.total_amount " +
            "FROM cart c JOIN products p ON c.product_id = p.product_id " +
            "WHERE c.session_id = ?";

        try (
            Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, sessionId);

            try (ResultSet rs = ps.executeQuery()) {

                out.print("[");
                boolean first = true;

                while (rs.next()) {

                    if (!first) {
                        out.print(",");
                    }

                    out.print("{");
                    out.print("\"productName\":\"" +
                            rs.getString("product_name").replace("\"", "\\\"") + "\",");
                    out.print("\"quantity\":" + rs.getDouble("quantity_kg") + ",");
                    out.print("\"pricePerKg\":" + rs.getDouble("price_per_kg") + ",");
                    out.print("\"totalAmount\":" + rs.getDouble("total_amount"));
                    out.print("}");

                    first = false;
                }

                out.print("]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("[]");
        }
    }
}
