package com.example.tasktracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/employee_task_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    public static Connection getConnection() throws SQLException {
        // Check for DATABASE_URL (Render PostgreSQL format)
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Render provides postgres:// but JDBC needs postgresql://
            if (databaseUrl.startsWith("postgres://")) {
                databaseUrl = databaseUrl.replace("postgres://", "postgresql://");
            }
            return DriverManager.getConnection(databaseUrl);
        }
        
        // Fallback to custom DB_URL, DB_USER, DB_PASS
        String url = getenvOrDefault("DB_URL", DEFAULT_URL);
        String user = getenvOrDefault("DB_USER", DEFAULT_USER);
        String pass = getenvOrDefault("DB_PASS", DEFAULT_PASS);
        return DriverManager.getConnection(url, user, pass);
    }

    private static String getenvOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }
}
