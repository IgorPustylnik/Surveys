package ru.vsu.cs.pustylnik_i_v.surveys.database.sql;

import java.sql.*;

public class DatabaseSource {
    public Connection getConnection() throws SQLException {
        Connection conn;
        try {
            Class.forName(DatabaseConfig.getDriver());
            conn = DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUsername(), DatabaseConfig.getPassword());
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver not found");
        }
        return conn;
    }
}
