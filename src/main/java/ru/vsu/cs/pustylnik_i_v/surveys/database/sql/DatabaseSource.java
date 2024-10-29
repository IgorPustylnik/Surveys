package ru.vsu.cs.pustylnik_i_v.surveys.database.sql;

import java.sql.*;

public class DatabaseSource {
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DatabaseConfig.getDriver());
            conn = DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUsername(), DatabaseConfig.getPassword());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not found");
        } catch (SQLException e) {
            System.err.println("SQL error");
        }
        return conn;
    }
}
