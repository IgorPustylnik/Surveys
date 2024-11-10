package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SessionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.sql.*;
import java.util.Date;

public class SessionSqlDAO extends BaseSqlDAO implements SessionDAO {
    public SessionSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Session getSessionById(int id) throws SessionNotFoundException, DatabaseAccessException {
        String query = "SELECT ss.id as session_id, ss.survey_id as survey_id, sv.name as survey_name, ss.user_id as user_id, " +
                "ss.started_at as started_at, ss.finished_at as finished_at FROM sessions ss " +
                "JOIN surveys sv ON ss.survey_id = sv.id " +
                "WHERE ss.id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer userId;
                    userId = resultSet.getInt("user_id");
                    if (resultSet.wasNull()) {
                        userId = null;
                    }
                    Timestamp finishedAt = resultSet.getTimestamp("finished_at");
                    if (resultSet.wasNull()) {
                        finishedAt = null;
                    }
                    return new Session(resultSet.getInt("session_id"),
                            resultSet.getInt("survey_id"),
                            resultSet.getString("survey_name"),
                            userId,
                            resultSet.getTimestamp("started_at"),
                            finishedAt);

                }
            } catch (SQLException e) {
                throw new SessionNotFoundException(id);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new SessionNotFoundException(id);
    }

    @Override
    public Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date endedAt) throws UserNotFoundException, SurveyNotFoundException, DatabaseAccessException {
        String queryCheckSurvey = "SELECT * FROM surveys WHERE id = ?";
        String queryCheckUser = "SELECT * FROM users WHERE id = ?";
        String query = "INSERT INTO sessions (survey_id, user_id, started_at) VALUES (?, ?, ?) RETURNING id";
        String queryUserNull = "INSERT INTO sessions (survey_id, started_at) VALUES (?, ?) RETURNING id";

        try (Connection connection = getConnection()) {
            PreparedStatement statementCheckSurvey;

            statementCheckSurvey = connection.prepareStatement(queryCheckSurvey);
            statementCheckSurvey.setInt(1, surveyId);

            if (!statementCheckSurvey.executeQuery().next()) {
                throw new SurveyNotFoundException(surveyId);
            }

            PreparedStatement statementCheckUser;
            if (userId != null) {
                statementCheckUser = connection.prepareStatement(queryCheckUser);
                statementCheckUser.setInt(1, userId);
                if (!statementCheckUser.executeQuery().next()) {
                    throw new UserNotFoundException(userId);
                }
            }

            PreparedStatement statement;
            if (userId != null) {
                statement = connection.prepareStatement(query);
                statement.setInt(1, surveyId);
                statement.setInt(2, userId);
                statement.setTimestamp(3, new Timestamp(startedAt.getTime()));
            } else {
                statement = connection.prepareStatement(queryUserNull);
                statement.setInt(1, surveyId);
                statement.setTimestamp(2, new Timestamp(startedAt.getTime()));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateSession(Session s) throws SessionNotFoundException, DatabaseAccessException {
        String checkQuery = "SELECT COUNT(*) FROM sessions WHERE id = ?";
        String updateQuery = "UPDATE sessions SET survey_id = ?, user_id = ?, started_at = ?, finished_at = ? WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, s.getId());
                try {
                    if (!checkStatement.execute()) {
                        throw new SessionNotFoundException(s.getId());
                    }
                } catch (SQLException e) {
                    throw new SessionNotFoundException(s.getId());
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, s.getSurveyId());
                if (s.getUserId() != null) {
                    updateStatement.setInt(2, s.getUserId());
                } else {
                    updateStatement.setNull(2, java.sql.Types.INTEGER);
                }
                updateStatement.setTimestamp(3, new Timestamp(s.getStartedAt().getTime()));
                updateStatement.setTimestamp(4, new Timestamp(s.getFinishedAt().getTime()));
                updateStatement.setInt(5, s.getId());

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public boolean exists(int id) throws DatabaseAccessException {
        String query = "SELECT COUNT(*) FROM sessions WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            if (statement.execute()) {
                return true;
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return false;
    }
}
