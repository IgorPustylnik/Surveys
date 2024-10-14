package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.PostgresqlDataSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSqlRepository extends BaseSqlRepository implements UserRepository {
    public UserSqlRepository(PostgresqlDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public User getUser(String name) throws UserNotFoundException {
        String query = "SELECT * FROM users WHERE name = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"));
                }
            }

        } catch (SQLException e) {
            throw new UserNotFoundException(name);
        }
        throw new UserNotFoundException(name);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM users";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(new User(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password")));
                }
            }
        } catch (SQLException ignored) {}
        return users;
    }

    @Override
    public void addUser(String name, String password) {
        String query = "INSERT INTO users (name, password) VALUES (?, ?)";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);
            statement.setString(2, password);

            statement.executeQuery();
        } catch (SQLException ignored) {}
    }

    @Override
    public void updateUser(User u) throws UserNotFoundException {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        String updateQuery = "UPDATE users SET name = ?, password = ? WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, u.getId());
                try {
                    if (!checkStatement.execute()) {
                        throw new UserNotFoundException(u.getName());
                    }
                } catch (SQLException e) {
                    throw new UserNotFoundException(u.getName());
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, u.getName());
                updateStatement.setString(2, u.getPassword());
                updateStatement.setInt(3, u.getId());

                updateStatement.executeUpdate();
            }
        } catch (SQLException ignored) {}
    }

    @Override
    public void deleteUser(String name) {
        String query = "DELETE FROM users WHERE name = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);

            statement.execute();
        } catch (SQLException ignored) {}
    }

    @Override
    public boolean exists(int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);

            if (statement.execute()) {
                return true;
            }

        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public boolean exists(String name) {
        try {
            getUser(name);
        } catch (UserNotFoundException e) {
            return false;
        }
        return true;
    }
}
