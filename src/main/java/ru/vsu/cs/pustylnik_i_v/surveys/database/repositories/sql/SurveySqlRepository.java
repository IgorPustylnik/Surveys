package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SurveyRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveySqlRepository extends BaseSqlRepository implements SurveyRepository {
    public SurveySqlRepository(DatabaseSource dataSource) {
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
                    statement.setInt(3, categoryId);
                    statement.setInt(4, authorId);
                    statement.setTimestamp(5, new java.sql.Timestamp(createdAt.getTime()));

                    PreparedStatement statementCategoryName = connection.prepareStatement(queryGetCategoryName);

                    statementCategoryName.setInt(1, categoryId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            returnee = new Survey(id, name, description, categoryId, null, authorName, createdAt);

                            try (ResultSet resultSet1 = statementCategoryName.executeQuery()) {
                                if (resultSet1.next()) {
                                    String categoryName = resultSet1.getString("name");
                                    returnee.setCategoryName(categoryName);
                                }
                            }
                        }
                    }
                }
            }

            return returnee;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public Survey getSurveyById(int id) throws SurveyNotFoundException, DatabaseAccessException {
        String query = "SELECT s.id as survey_id, s.name as survey_name, s.description, s.category_id, c.name as category_name, u.name as author_name, s.created_at " +
                "FROM surveys s LEFT JOIN categories c ON s.category_id = c.id LEFT JOIN users u ON s.author_id = u.id AND s.id = ?";

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
    public void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException, DatabaseAccessException {
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
    public PagedEntity<List<Survey>> getSurveysPagedEntity(Integer categoryId, Integer page, Integer perPageAmount) throws CategoryNotFoundException, DatabaseAccessException {
        List<Survey> surveys = new ArrayList<>();

        int fromIndex = perPageAmount * page;
        int totalCount = 0;

        String query = "SELECT s.id AS survey_id, s.name AS survey_name, s.description, s.category_id, u.name as author_name, s.created_at, " +
                "c.name AS category_name " +
                "FROM surveys s " +
                "LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN users u ON s.author_id = u.id " +
                "WHERE s.category_id = ? " +
                "ORDER BY s.id " +
                "LIMIT ? OFFSET ?";
        String queryTotalCount = "SELECT COUNT(*) FROM surveys WHERE category_id = ?";

        String queryAll = "SELECT s.id AS survey_id, s.name AS survey_name, s.description, s.category_id, u.name as author_name, s.created_at, " +
                "c.name AS category_name " +
                "FROM surveys s " +
                "LEFT JOIN categories c ON s.category_id = c.id " +
                "LEFT JOIN users u ON s.author_id = u.id " +
                "ORDER BY s.id " +
                "LIMIT ? OFFSET ?";
        String queryTotalCountAll = "SELECT COUNT(*) FROM surveys";

        String queryCheckCategory = "SELECT * FROM categories WHERE id = ?";

        try (Connection connection = getConnection()) {

            PreparedStatement statement;
            PreparedStatement statementTotalCount;

            if (categoryId != null) {
                statement = connection.prepareStatement(query);
                statementTotalCount = connection.prepareStatement(queryTotalCount);

                statement.setInt(1, categoryId);
                statement.setInt(2, perPageAmount);
                statement.setInt(3, fromIndex);

                statementTotalCount.setInt(1, categoryId);

                PreparedStatement statementCheck = connection.prepareStatement(queryCheckCategory);

                statementCheck.setInt(1, categoryId);

                boolean categoryExists = statementCheck.execute();

                if (!categoryExists) {
                    throw new CategoryNotFoundException(categoryId);
                }
            } else {
                statement = connection.prepareStatement(queryAll);
                statementTotalCount = connection.prepareStatement(queryTotalCountAll);

                statement.setInt(1, perPageAmount);
                statement.setInt(2, fromIndex);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    surveys.add(new Survey(resultSet.getInt("survey_id"),
                            resultSet.getString("survey_name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name"),
                            resultSet.getString("author_name"),
                            resultSet.getTimestamp("created_at")));
                }
            }

            try (ResultSet resultSetTotalCount = statementTotalCount.executeQuery()) {
                while (resultSetTotalCount.next()) {
                    totalCount = resultSetTotalCount.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
