package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SessionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.sql.*;
import java.util.Date;

public class SessionSqlRepository extends BaseSqlRepository implements SessionRepository {
    public SessionSqlRepository(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Session getSessionById(int id) throws SessionNotFoundException, DatabaseAccessException {
        String query = "SELECT * FROM sessions WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Session(resultSet.getInt("id"),
                            resultSet.getInt("survey_id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("started_at"),
                            resultSet.getTimestamp("finished_at"));

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
    public Session getUserSession(int userId) throws SessionNotFoundException, DatabaseAccessException, UserNotFoundException {
        String queryCheckUser = "SELECT * FROM users WHERE id = ?";
        String query = "SELECT * FROM sessions WHERE user_id = ?";

        try (Connection connection = getConnection()) {

            PreparedStatement statementCheckUser;
            statementCheckUser = connection.prepareStatement(queryCheckUser);
            statementCheckUser.setInt(1, userId);
            if (!statementCheckUser.executeQuery().next()) {
                throw new UserNotFoundException(userId);
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Session(resultSet.getInt("id"),
                            resultSet.getInt("survey_id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("started_at"),
                            resultSet.getTimestamp("finished_at"));

                }
            } catch (SQLException e) {
                throw new SessionNotFoundException(-1);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new SessionNotFoundException(-1);
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
        String updateQuery = "UPDATE sessions SET finished_at = ? WHERE id = ?";

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
                updateStatement.setTimestamp(1, new Timestamp(s.getFinishedAt().getTime()));
                updateStatement.setInt(2, s.getId());

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
