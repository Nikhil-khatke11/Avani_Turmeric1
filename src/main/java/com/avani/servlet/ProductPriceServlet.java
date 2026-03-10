package com.avani.servlet;

import com.avani.util.DatabaseConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/getProductPrice")
public class ProductPriceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String pid = request.getParameter("productId");
        int productId;

        try {
            productId = Integer.parseInt(pid);
        } catch (Exception e) {
            response.sendError(400, "Invalid productId");
            return;
        }

        String sql = "SELECT price_per_kg FROM products WHERE product_id = ?";

        try (
            Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                out.print(rs.getDouble("price_per_kg"));
            } else {
                out.print("200");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("0");
        }
    }
}
