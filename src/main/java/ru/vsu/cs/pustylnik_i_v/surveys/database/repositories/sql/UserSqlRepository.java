package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSqlRepository extends BaseSqlRepository implements UserRepository {
    public UserSqlRepository(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public User getUser(String name) throws UserNotFoundException, DatabaseAccessException {
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
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new UserNotFoundException(name);
    }

    @Override
    public User getUser(int id) throws UserNotFoundException, DatabaseAccessException {
        String query = "SELECT * FROM users WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new UserNotFoundException(id);
    }

    @Override
    public List<User> getAllUsers() throws DatabaseAccessException {
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
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return users;
    }

    @Override
    public PagedEntity<List<User>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        List<User> users = new ArrayList<>();

        int fromIndex = perPageAmount * page;
        int totalCount = 0;

        String query = "SELECT * FROM users LIMIT ? OFFSET ?";
        String queryTotalCount = "SELECT COUNT(*) FROM users";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statementTotalCount = connection.prepareStatement(queryTotalCount);

            statement.setInt(1, perPageAmount);
            statement.setInt(2, fromIndex);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("password")));
                }
            }

            try (ResultSet resultSetTotalCount = statementTotalCount.executeQuery()) {
                while (resultSetTotalCount.next()) {
                    totalCount = resultSetTotalCount.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }

        int totalPages = (int) Math.ceil((double) totalCount / perPageAmount);
        if (totalPages < 1) totalPages = 1;

        return new PagedEntity<>(page, totalPages, users);
    }

    @Override
    public void addUser(String name, String password) throws DatabaseAccessException {
        String query = "INSERT INTO users (name, password) VALUES (?, ?)";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);
            statement.setString(2, password);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User u) throws UserNotFoundException, DatabaseAccessException {
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
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(String name) throws DatabaseAccessException {
        String query = "DELETE FROM users WHERE name = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public boolean exists(int userId) throws DatabaseAccessException {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);

            if (statement.execute()) {
                return true;
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean exists(String name) {
        try {
            getUser(name);
        } catch (UserNotFoundException | DatabaseAccessException e) {
            return false;
        }
        return true;
    }
}
