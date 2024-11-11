package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.AnswerDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AnswerSqlDAO extends BaseSqlDAO implements AnswerDAO {

    public AnswerSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public void putAnswersToQuestion(int sessionId, List<Integer> optionIds) throws SessionNotFoundException, OptionNotFoundException, DatabaseAccessException {
        String checkSessionQuery = "SELECT * FROM sessions WHERE id = ?";
        String checkOptionQuery = "SELECT * FROM options WHERE id = ?";

        String query = "INSERT INTO answers (session_id, option_id) VALUES (?, ?)";
        String queryDeleteOld = "DELETE FROM answers " +
                "WHERE option_id IN (" +
                "SELECT o.id " +
                "FROM options o " +
                "JOIN questions q ON o.question_id = q.id " +
                "WHERE q.id = (SELECT question_id FROM options WHERE id = ?)) " +
                "AND session_id = ?;";

        try (Connection connection = getConnection()) {

            // Delete all answers
            try {
                PreparedStatement deleteOldStatement = connection.prepareStatement(queryDeleteOld);
                deleteOldStatement.setInt(1, optionIds.get(0));
                deleteOldStatement.setInt(2, sessionId);

                deleteOldStatement.execute();
            } catch (SQLException e) {
                throw new DatabaseAccessException(e.getMessage());
            }

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

            if (optionIds.isEmpty()) {
                return;
            }

            // Check if option exists
            for (int optionId : optionIds) {
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
            }
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
