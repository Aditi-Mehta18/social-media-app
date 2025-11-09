package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/social_media_app";
    private static final String USER = "root";
    private static final String PASSWORD = "Aditi1206@";

    private static Connection connection;

    private MySQLConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}