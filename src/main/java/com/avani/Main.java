package com.avani;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.avani.filter.CorsFilter;
import com.avani.servlet.AddToCartServlet;
import com.avani.servlet.PlaceOrderServlet;
import com.avani.servlet.ProductPriceServlet;
import com.avani.servlet.ViewCartServlet;
import com.avani.util.DatabaseConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("🚀 Starting Avani Turmeric Backend...");

            // Use PORT from environment (Render sets this), default 8080
            int port = Integer.parseInt(
                System.getenv("PORT") != null ? System.getenv("PORT") : "8080"
            );

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(port);
            tomcat.getConnector().setProperty("address", "0.0.0.0");

            // Create context
            String docBase = new File(".").getAbsolutePath();
            Context ctx = tomcat.addContext("", docBase);

            // Register servlets
            Tomcat.addServlet(ctx, "addToCart", new AddToCartServlet());
            ctx.addServletMappingDecoded("/addToCart", "addToCart");

            Tomcat.addServlet(ctx, "viewCart", new ViewCartServlet());
            ctx.addServletMappingDecoded("/viewCart", "viewCart");

            Tomcat.addServlet(ctx, "getProductPrice", new ProductPriceServlet());
            ctx.addServletMappingDecoded("/getProductPrice", "getProductPrice");

            Tomcat.addServlet(ctx, "placeOrder", new PlaceOrderServlet());
            ctx.addServletMappingDecoded("/placeOrder", "placeOrder");

            // Register CORS filter using Tomcat's native API
            FilterDef filterDef = new FilterDef();
            filterDef.setFilterName("CorsFilter");
            filterDef.setFilterClass(CorsFilter.class.getName());
            ctx.addFilterDef(filterDef);

            FilterMap filterMap = new FilterMap();
            filterMap.setFilterName("CorsFilter");
            filterMap.addURLPattern("/*");
            ctx.addFilterMap(filterMap);

            // Start Tomcat
            tomcat.start();
            System.out.println("✅ Tomcat started on port " + port);

            // Initialize DB after Tomcat starts (non-blocking)
            initializeDatabase();

            System.out.println("✅ Avani Turmeric Backend is LIVE on port " + port);
            tomcat.getServer().await();

        } catch (Exception e) {
            System.err.println("❌ Failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initializeDatabase() {
        System.out.println("🔧 Initializing database schema...");
        try (Connection con = DatabaseConfig.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("CREATE SEQUENCE IF NOT EXISTS cart_seq START WITH 1 INCREMENT BY 1");
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS customer_seq START WITH 1 INCREMENT BY 1");
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS order_seq START WITH 1 INCREMENT BY 1");
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 1");

            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                "product_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('product_seq'), " +
                "product_name VARCHAR(255) NOT NULL, " +
                "price_per_kg DECIMAL(10, 2) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("INSERT INTO products (product_id, product_name, price_per_kg) " +
                "VALUES (1, 'Avani Turmeric Powder', 300.00) " +
                "ON CONFLICT (product_id) DO NOTHING");

            stmt.execute("CREATE TABLE IF NOT EXISTS cart (" +
                "cart_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('cart_seq'), " +
                "product_id INTEGER NOT NULL, " +
                "quantity_kg DECIMAL(10, 2) NOT NULL, " +
                "price_per_kg DECIMAL(10, 2) NOT NULL, " +
                "total_amount DECIMAL(10, 2) NOT NULL, " +
                "session_id VARCHAR(255) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (product_id) REFERENCES products(product_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS customer_details (" +
                "customer_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('customer_seq'), " +
                "full_name VARCHAR(255) NOT NULL, " +
                "address TEXT NOT NULL, " +
                "city VARCHAR(100) NOT NULL, " +
                "state VARCHAR(100) NOT NULL, " +
                "pin_code VARCHAR(6) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                "order_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('order_seq'), " +
                "customer_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "quantity_kg DECIMAL(10, 2) NOT NULL, " +
                "total_amount DECIMAL(10, 2) NOT NULL, " +
                "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "status VARCHAR(50) DEFAULT 'PENDING', " +
                "FOREIGN KEY (customer_id) REFERENCES customer_details(customer_id), " +
                "FOREIGN KEY (product_id) REFERENCES products(product_id))");

            stmt.execute("CREATE INDEX IF NOT EXISTS idx_cart_session ON cart(session_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date)");

            System.out.println("✅ Database schema initialized!");

        } catch (Exception e) {
            System.err.println("⚠️ DB init warning (non-fatal): " + e.getMessage());
        }
    }
}
