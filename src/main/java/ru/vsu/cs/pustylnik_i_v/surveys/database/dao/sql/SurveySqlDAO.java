package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SurveyDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveySqlDAO extends BaseSqlDAO implements SurveyDAO {
    public SurveySqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Survey addSurvey(String name, String description, Integer categoryId, String authorName, Date createdAt) throws DatabaseAccessException {
        String query = "INSERT INTO surveys (name, description, category_id, author_id, created_at) VALUES (?, ?, ?, ?, ?) RETURNING id";

        String queryGetUserId = "SELECT id FROM users WHERE name = ?";
        String queryGetCategoryName = "SELECT name FROM categories WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statementUserId = connection.prepareStatement(queryGetUserId);
            statementUserId.setString(1, authorName);

            Survey returnee = null;

            int authorId;
            try (ResultSet resultSetUserId = statementUserId.executeQuery()) {
                if (resultSetUserId.next()) {
                    authorId = resultSetUserId.getInt("id");

                    PreparedStatement statement = connection.prepareStatement(query);

                    statement.setString(1, name);
                    statement.setString(2, description);
                    if (categoryId != null) {
                        statement.setInt(3, categoryId);
                    } else {
                        statement.setNull(3, Types.INTEGER);
                    }
                    statement.setInt(4, authorId);
                    statement.setTimestamp(5, new Timestamp(createdAt.getTime()));

                    PreparedStatement statementCategoryName = connection.prepareStatement(queryGetCategoryName);

                    statementCategoryName.setInt(1, categoryId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            returnee = new Survey(id, name, description, categoryId, null, authorName, 0, createdAt);

                            try (ResultSet resultSet1 = statementCategoryName.executeQuery()) {
                                if (resultSet1.next()) {
                                    String categoryName = resultSet1.getString("name");
                                    returnee.setCategoryName(categoryName);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                return null;
            }

            return returnee;

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public Survey getSurveyById(int id) throws SurveyNotFoundException, DatabaseAccessException {
        String query = "SELECT s.id AS survey_id, s.name AS survey_name, s.description, s.category_id, " +
                "c.name AS category_name, u.name AS author_name, s.created_at, " +
                "COUNT(q.id) AS question_count " +
                "FROM surveys s " +
                "LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN users u ON s.author_id = u.id " +
                "LEFT JOIN questions q ON q.survey_id = s.id " +
                "WHERE s.id = ? " +
                "GROUP BY s.id, c.name, u.name";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Survey(resultSet.getInt("survey_id"),
                            resultSet.getString("survey_name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name"),
                            resultSet.getString("author_name"),
                            resultSet.getInt("question_count"),
                            resultSet.getTimestamp("created_at"));
                }
            } catch (SQLException e) {
                throw new SurveyNotFoundException(id);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new SurveyNotFoundException(id);
    }


    @Override
    public void updateSurveyCategory(int id, Integer categoryId) throws SurveyNotFoundException, DatabaseAccessException {
        String checkQuery = "SELECT COUNT(*) FROM surveys WHERE id = ?";
        String updateQuery = "UPDATE surveys SET category_id = ? WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, id);
                try {
                    if (!checkStatement.execute()) {
                        throw new SurveyNotFoundException(id);
                    }
                } catch (SQLException e) {
                    throw new SurveyNotFoundException(id);
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, categoryId);
                updateStatement.setInt(2, id);

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public PagedEntity<List<Survey>> getSurveysPagedEntity(String authorName, Integer categoryId, Date fromDate, Date toDate, int page, int perPageAmount)
            throws CategoryNotFoundException, DatabaseAccessException {

        List<Survey> surveys = new ArrayList<>();
        int fromIndex = perPageAmount * page;
        int totalCount = 0;

        String query = "SELECT s.id AS survey_id, s.name AS survey_name, s.description, s.category_id, u.name AS author_name, " +
                "s.created_at, c.name AS category_name, COUNT(q.id) AS question_count " +
                "FROM surveys s " +
                "LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN users u ON s.author_id = u.id " +
                "LEFT JOIN questions q ON q.survey_id = s.id " +
                "WHERE 1=1 ";

        if (authorName != null) {
            query += "AND u.name = ? ";
        }
        if (categoryId != null) {
            query += "AND s.category_id = ? ";
        }
        if (fromDate != null) {
            query += "AND s.created_at >= ? ";
        }
        if (toDate != null) {
            query += "AND s.created_at <= ? ";
        }

        query += "GROUP BY s.id, c.name, u.name ORDER BY s.id LIMIT ? OFFSET ?";

        String queryTotalCount = "SELECT COUNT(*) FROM surveys WHERE 1=1 ";
        if (authorName != null) {
            queryTotalCount += "AND author_id = (SELECT id FROM users WHERE name = ?) ";
        }
        if (categoryId != null) {
            queryTotalCount += "AND category_id = ? ";
        }
        if (fromDate != null) {
            queryTotalCount += "AND created_at >= ? ";
        }
        if (toDate != null) {
            queryTotalCount += "AND created_at <= ? ";
        }

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statementTotalCount = connection.prepareStatement(queryTotalCount);

            int paramIndex = 1;

            if (authorName != null) {
                statement.setString(paramIndex, authorName);
                statementTotalCount.setString(paramIndex, authorName);
                paramIndex++;
            }

            if (categoryId != null) {
                statement.setInt(paramIndex, categoryId);
                statementTotalCount.setInt(paramIndex, categoryId);
                paramIndex++;
            }

            if (fromDate != null) {
                statement.setTimestamp(paramIndex, new Timestamp(fromDate.getTime()));
                statementTotalCount.setTimestamp(paramIndex, new Timestamp(fromDate.getTime()));
                paramIndex++;
            }
            if (toDate != null) {
                statement.setTimestamp(paramIndex, new Timestamp(toDate.getTime()));
                statementTotalCount.setTimestamp(paramIndex, new Timestamp(toDate.getTime()));
                paramIndex++;
            }

            statement.setInt(paramIndex++, perPageAmount);
            statement.setInt(paramIndex, fromIndex);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    surveys.add(new Survey(resultSet.getInt("survey_id"),
                            resultSet.getString("survey_name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name"),
                            resultSet.getString("author_name"),
                            resultSet.getInt("question_count"),
                            resultSet.getTimestamp("created_at")));
                }
            }

            try (ResultSet resultSetTotalCount = statementTotalCount.executeQuery()) {
                if (resultSetTotalCount.next()) {
                    totalCount = resultSetTotalCount.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }

        int totalPages = (int) Math.ceil((double) totalCount / perPageAmount);
        if (totalPages < 1) totalPages = 1;

        return new PagedEntity<>(page, totalPages, surveys);
    }


    @Override
    public void deleteSurvey(int id) throws DatabaseAccessException {
        String query = "DELETE FROM surveys WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public boolean exists(int id) throws DatabaseAccessException {
        String query = "SELECT COUNT(*) FROM surveys WHERE id = ?";

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
