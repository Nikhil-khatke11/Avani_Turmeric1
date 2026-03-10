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

    /**
     * Get database connection based on environment
     * 
     * @return Connection to appropriate database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if ("postgres".equalsIgnoreCase(DB_TYPE)) {
            // Cloud deployment with PostgreSQL
            return DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
        } else {
            // Local development with Oracle
            return DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASSWORD);
        }
    }

    /**
     * Get the appropriate SQL for sequence/auto-increment
     * Oracle uses sequences, PostgreSQL uses SERIAL/NEXTVAL
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
