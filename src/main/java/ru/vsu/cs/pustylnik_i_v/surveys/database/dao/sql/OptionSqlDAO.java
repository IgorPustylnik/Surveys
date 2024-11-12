package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.OptionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OptionSqlDAO extends BaseSqlDAO implements OptionDAO {

    public OptionSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Option getOption(int id) throws OptionNotFoundException, DatabaseAccessException {
        String query = "SELECT * FROM options WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Option(resultSet.getInt("id"),
                            resultSet.getInt("question_id"),
                            resultSet.getString("description"));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new OptionNotFoundException(id);
    }

    @Override
    public void addOption(int questionId, String description) throws QuestionNotFoundException, DatabaseAccessException {
        String query = "INSERT INTO options (question_id, description) VALUES (?, ?)";
        String queryCheckQuestion = "SELECT COUNT(*) FROM questions WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement checkStatement = connection.prepareStatement(queryCheckQuestion);
            checkStatement.setInt(1, questionId);

            if (!checkStatement.execute()) {
                throw new QuestionNotFoundException(questionId);
            }

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, questionId);
            statement.setString(2, description);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void updateOption(Option option) throws DatabaseAccessException, OptionNotFoundException {
        String checkQuery = "SELECT COUNT(*) FROM options WHERE id = ?";
        String updateQuery = "UPDATE options SET (question_id, description) = (?, ?) WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, option.getId());
                try {
                    if (!checkStatement.execute()) {
                        throw new OptionNotFoundException(option.getId());
                    }
                } catch (SQLException e) {
                    throw new OptionNotFoundException(option.getId());
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, option.getQuestionId());
                updateStatement.setString(2, option.getDescription());
                updateStatement.setInt(3, option.getId());

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteOption(int id) throws DatabaseAccessException {
        String query = "DELETE FROM options WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }
}
