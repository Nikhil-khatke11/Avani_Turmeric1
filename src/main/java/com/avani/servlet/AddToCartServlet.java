package com.avani.servlet;

import com.avani.util.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String productIdParam = req.getParameter("productId");
        String quantityParam = req.getParameter("quantity");
        String priceParam = req.getParameter("pricePerKg");

        if (productIdParam == null || quantityParam == null || priceParam == null ||
            productIdParam.trim().isEmpty() ||
            quantityParam.trim().isEmpty() ||
            priceParam.trim().isEmpty()) {

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing product data");
            return;
        }

        int productId;
        double quantity;
        double pricePerKg;

        try {
            productId = Integer.parseInt(productIdParam);
            quantity = Double.parseDouble(quantityParam);
            pricePerKg = Double.parseDouble(priceParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid numeric values");
            return;
        }

        double totalAmount = quantity * pricePerKg;
        String sessionId = req.getSession().getId();

        String sql =
            "INSERT INTO cart " +
            "(cart_id, product_id, quantity_kg, price_per_kg, total_amount, session_id) " +
            "VALUES (" + DatabaseConfig.getNextValSyntax("cart_seq") + ", ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setDouble(2, quantity);
            ps.setDouble(3, pricePerKg);
            ps.setDouble(4, totalAmount);
            ps.setString(5, sessionId);

            ps.executeUpdate();

            // Return JSON success for AJAX calls (cross-origin frontend)
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Added to cart\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }
}