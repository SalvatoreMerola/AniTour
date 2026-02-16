package com.anitour.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Parametri fissi come da Test Plan
    private static final String URL = "jdbc:mysql://localhost:3306/anitourdb";
    private static final String USER = "root";     // User
    private static final String PASSWORD = "root"; // Password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trovato", e);
        }
    }
}