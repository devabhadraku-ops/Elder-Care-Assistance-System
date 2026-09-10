package com.eldercare.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
            "jdbc:mysql://localhost:3306/EldercareAssistanceSystem"
        );

        config.setUsername("root");

        // We will set your MySQL password separately.
        config.setPassword(System.getenv("DB_PASSWORD"));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void main(String[] args) {
    try (Connection connection = getConnection()) {
        System.out.println("Database connected successfully!");
    } catch (SQLException e) {
        System.out.println("Database connection failed!");
        e.printStackTrace();
    }
}
}