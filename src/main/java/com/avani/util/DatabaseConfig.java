package com.avani.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database utility class with environment-based configuration
 * Supports both Oracle (local) and PostgreSQL (cloud deployment)
 */
public class DatabaseConfig {

    private static final String DB_TYPE = System.getenv("DB_TYPE") != null ? System.getenv("DB_TYPE") : "oracle";

    // Oracle settings (local development)
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521/ORCLPDB";
    private static final String ORACLE_USER = "avani_db";
    private static final String ORACLE_PASSWORD = "avani123";

    // PostgreSQL settings (cloud deployment - from environment variables)
    private static final String POSTGRES_URL = System.getenv("DATABASE_URL");
    private static final String POSTGRES_USER = System.getenv("DB_USER");
    private static final String POSTGRES_PASSWORD = System.getenv("DB_PASSWORD");

    static {
        // Explicitly load JDBC drivers
        try {
            if ("postgres".equalsIgnoreCase(DB_TYPE)) {
                Class.forName("org.postgresql.Driver");
                System.out.println("✅ PostgreSQL JDBC driver loaded");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC driver not found: " + e.getMessage());
        }
    }

    /**
     * Get database connection based on environment
     */
    public static Connection getConnection() throws SQLException {
        if ("postgres".equalsIgnoreCase(DB_TYPE)) {
            System.out.println("🔗 Connecting to PostgreSQL: " + POSTGRES_URL);
            return DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
        } else {
            return DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
        }
    }

    /**
     * Get the appropriate SQL for sequence/auto-increment
     */
    public static String getNextValSyntax(String sequenceName) {
        if ("postgres".equalsIgnoreCase(DB_TYPE)) {
            return "NEXTVAL('" + sequenceName + "')";
        } else {
            return sequenceName + ".NEXTVAL";
        }
    }

    /**
     * Get current value syntax for different databases
     */
    public static String getCurrentValQuery(String sequenceName) {
        if ("postgres".equalsIgnoreCase(DB_TYPE)) {
            return "SELECT CURRVAL('" + sequenceName + "')";
        } else {
            return "SELECT " + sequenceName + ".CURRVAL FROM dual";
        }
    }
}
