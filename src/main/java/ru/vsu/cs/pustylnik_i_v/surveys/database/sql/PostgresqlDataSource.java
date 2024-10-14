package ru.vsu.cs.pustylnik_i_v.surveys.database.sql;

import java.sql.*;

public class PostgresqlDataSource {
    private static final String DB_Driver = "org.postgresql.Driver";

    private final String url;
    private final String user;
    private final String password;

    public PostgresqlDataSource(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DB_Driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not found");
        } catch (SQLException e) {
            System.err.println("SQL error");
        }
        return conn;
    }
}
