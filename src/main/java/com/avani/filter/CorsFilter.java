package com.avani.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CORS Filter to allow frontend (Netlify) to communicate with backend
 * (Railway/Render)
 * This enables Cross-Origin Resource Sharing for the split deployment
 * architecture
 */
@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the origin from environment variable (will be set in Railway/Render)
        String allowedOrigin = System.getenv("FRONTEND_URL");

        // Fallback for local development
        if (allowedOrigin == null || allowedOrigin.isEmpty()) {
            allowedOrigin = "https://avani-turmerics.netlify.app";
        }

        // Set CORS headers
        httpResponse.setHeader("Access-Control-Allow-Origin", allowedOrigin);
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
