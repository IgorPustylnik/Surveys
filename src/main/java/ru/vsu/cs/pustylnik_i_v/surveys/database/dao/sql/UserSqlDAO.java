package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.UserDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
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

public class UserSqlDAO extends BaseSqlDAO implements UserDAO {
    public UserSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public User getUser(String name) throws UserNotFoundException, DatabaseAccessException {
        String query = "SELECT u.id as user_id, u.name as user_name, role, password " +
                "FROM users u JOIN roles r ON u.id = r.user_id WHERE u.name = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String roleString = resultSet.getString("role");
                    RoleType roleType = roleString != null ? RoleType.valueOf(roleString.toUpperCase()) : null;
                    return new User(resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            roleType,
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
        String query = "SELECT u.id as user_id, u.name as user_name, role, password " +
                "FROM users u LEFT JOIN roles r ON u.id = r.user_id WHERE u.id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String roleString = resultSet.getString("role");
                    RoleType roleType = roleString != null ? RoleType.valueOf(roleString.toUpperCase()) : null;
                    return new User(resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            roleType,
                            resultSet.getString("password"));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new UserNotFoundException(id);
    }

    @Override
    public PagedEntity<List<User>> getUsersPagedList(int page, int perPageAmount) throws DatabaseAccessException {
        List<User> users = new ArrayList<>();

        int fromIndex = perPageAmount * page;
        int totalCount = 0;

        String query = "SELECT u.id as user_id, u.name as user_name, role, password FROM users u LEFT JOIN roles r " +
                "ON u.id = r.user_id LIMIT ? OFFSET ?";
        String queryTotalCount = "SELECT COUNT(*) FROM users LEFT JOIN roles ON users.id = roles.user_id";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statementTotalCount = connection.prepareStatement(queryTotalCount);

            statement.setInt(1, perPageAmount);
            statement.setInt(2, fromIndex);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String roleString = resultSet.getString("role");
                    RoleType roleType = roleString != null ? RoleType.valueOf(roleString.toUpperCase()) : null;
                    users.add(new User(resultSet.getInt("user_id"),
                            resultSet.getString("user_name"),
                            roleType,
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
    public void addUser(String name, RoleType roleType, String password) throws DatabaseAccessException {
        String queryAddUser = "INSERT INTO users (name, password) VALUES (?, ?) RETURNING id";
        String queryAddRole = "INSERT INTO roles (user_id, role) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement statementAddUser = connection.prepareStatement(queryAddUser);

            statementAddUser.setString(1, name);
            statementAddUser.setString(2, password);

            int userId;
            try (ResultSet resultSet = statementAddUser.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");

                    PreparedStatement statementAddRole = connection.prepareStatement(queryAddRole);

                    statementAddRole.setInt(1, userId);
                    statementAddRole.setObject(2, roleType.toString().toLowerCase(), java.sql.Types.OTHER);

                    statementAddRole.execute();
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User u) throws UserNotFoundException, DatabaseAccessException {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        String updateQuery = "UPDATE users SET name = ?, password = ? WHERE id = ?;" +
                "UPDATE roles SET role = ? WHERE user_id = ?;";

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
                updateStatement.setObject(4, u.getRole().toString().toLowerCase(), java.sql.Types.OTHER);
                updateStatement.setInt(5, u.getId());

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(String name) throws DatabaseAccessException {
        String query = "DELETE FROM users WHERE name = ?";

        try (Connection connection = getConnection()) {
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

        try (Connection connection = getConnection()) {
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
    public boolean exists(String name) throws DatabaseAccessException {
        try {
            getUser(name);
        } catch (UserNotFoundException e) {
            return false;
        }
        return true;
    }
}
