package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.AnswerRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnswerSqlRepository extends BaseSqlRepository implements AnswerRepository {

    public AnswerSqlRepository(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) throws SessionNotFoundException, OptionNotFoundException, DatabaseAccessException {
        String checkSessionQuery = "SELECT * FROM sessions WHERE id = ?";
        String checkOptionQuery = "SELECT * FROM options WHERE id = ?";

        String query = "INSERT INTO answers (session_id, option_id) VALUES (?, ?)";

        try (Connection connection = getConnection()) {

            // Check if session exists
            try {
                PreparedStatement checkSessionStatement = connection.prepareStatement(checkSessionQuery);
                checkSessionStatement.setInt(1, sessionId);
                if (!checkSessionStatement.execute()) {
                    throw new SessionNotFoundException(sessionId);
                }
            } catch (SQLException e) {
                throw new SessionNotFoundException(sessionId);
            }

            // Check if option exists
            try {
                PreparedStatement checkSessionStatement = connection.prepareStatement(checkOptionQuery);
                checkSessionStatement.setInt(1, optionId);
                if (!checkSessionStatement.execute()) {
                    throw new OptionNotFoundException(optionId);
                }
            } catch (SQLException e) {
                throw new OptionNotFoundException(optionId);
            }

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, sessionId);
            statement.setInt(2, optionId);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public boolean exists(int sessionId, int optionId) throws DatabaseAccessException {
        String query = "SELECT COUNT(*) FROM answers WHERE session_id = ? AND option_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, sessionId);
            statement.setInt(2, optionId);

            if (statement.execute()) {
                return true;
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return false;
    }
}
