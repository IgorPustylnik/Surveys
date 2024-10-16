package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base;

import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.PostgresqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseSqlRepository {
    private final PostgresqlDataSource dataSource;

    public BaseSqlRepository(PostgresqlDataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Failed to obtain a database connection.");
        }
        return connection;
    }
}
