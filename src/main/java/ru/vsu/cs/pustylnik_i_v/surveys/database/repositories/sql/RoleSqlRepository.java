package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.RoleRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleSqlRepository extends BaseSqlRepository implements RoleRepository {
    public RoleSqlRepository(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Role getRole(int userId) throws UserNotFoundException {
        String query = "SELECT * FROM roles WHERE user_id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Role(resultSet.getInt("user_id"),
                            RoleType.valueOf(resultSet.getString("role").toUpperCase()));
                }
            }

        } catch (SQLException e) {
            throw new UserNotFoundException(userId);
        }
        throw new UserNotFoundException(userId);
    }

    @Override
    public void addRole(int userId, RoleType roleType) {
        String query = "INSERT INTO roles (user_id, role) VALUES (?, ?)";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);
            statement.setObject(2, roleType.toString().toLowerCase(), java.sql.Types.OTHER);

            statement.execute();
        } catch (SQLException ignored) {}
    }

    @Override
    public void updateRole(int userId, RoleType roleType) throws UserNotFoundException {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        String updateQuery = "UPDATE roles SET role = ? WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, userId);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0) {
                        throw new UserNotFoundException(userId);
                    }
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setObject(1, roleType.toString().toLowerCase(), java.sql.Types.OTHER);
                updateStatement.setInt(2, userId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException ignored) {}
    }


    @Override
    public boolean exists(int userId) {
        try {
            getRole(userId);
        } catch (UserNotFoundException e) {
            return false;
        }
        return true;
    }
}
