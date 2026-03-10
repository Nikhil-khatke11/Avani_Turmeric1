package com.avani;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import com.avani.filter.CorsFilter;
import com.avani.servlet.AddToCartServlet;
import com.avani.servlet.PlaceOrderServlet;
import com.avani.servlet.ProductPriceServlet;
import com.avani.servlet.ViewCartServlet;

import jakarta.servlet.FilterRegistration;

import java.io.File;

/**
 * Embedded Tomcat launcher for cloud deployment (Render.com)
 * Runs without external Tomcat installation
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // Use PORT from environment (Render sets this), default 8080
        int port = Integer.parseInt(
            System.getenv("PORT") != null ? System.getenv("PORT") : "8080"
        );

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Initialize default connector

        // Create context
        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext(contextPath, docBase);

        // Register servlets
        Tomcat.addServlet(ctx, "addToCart", new AddToCartServlet());
        ctx.addServletMappingDecoded("/addToCart", "addToCart");

        Tomcat.addServlet(ctx, "viewCart", new ViewCartServlet());
        ctx.addServletMappingDecoded("/viewCart", "viewCart");

        Tomcat.addServlet(ctx, "getProductPrice", new ProductPriceServlet());
        ctx.addServletMappingDecoded("/getProductPrice", "getProductPrice");

        Tomcat.addServlet(ctx, "placeOrder", new PlaceOrderServlet());
        ctx.addServletMappingDecoded("/placeOrder", "placeOrder");

        // Register CORS filter
        FilterRegistration.Dynamic corsFilter = ctx.getServletContext()
                .addFilter("CorsFilter", new CorsFilter());
        corsFilter.addMappingForUrlPatterns(null, false, "/*");

        // Start Tomcat
        tomcat.start();
        System.out.println("✅ Avani Turmeric Backend started on port " + port);
        tomcat.getServer().await();
    }
}
