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
    public Survey addSurvey(String name, String description, Integer categoryId, Date createdAt) throws DatabaseAccessException {
        String query = "INSERT INTO surveys (name, description, category_id, created_at) VALUES (?, ?, ?, ?) RETURNING id";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, categoryId);
            statement.setTimestamp(4, new java.sql.Timestamp(createdAt.getTime()));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    return new Survey(id, name, description, categoryId, createdAt);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public Survey getSurveyById(int id) throws SurveyNotFoundException, DatabaseAccessException {
        String query = "SELECT * FROM surveys WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Survey(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
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

        String query = "SELECT * FROM surveys WHERE category_id = ? LIMIT ? OFFSET ?";
        String queryTotalCount = "SELECT COUNT(*) FROM surveys WHERE category_id = ?";

        String queryAll = "SELECT * FROM surveys LIMIT ? OFFSET ?";
        String queryTotalCountAll = "SELECT COUNT(*) FROM surveys";

        String queryCheckCategory = "SELECT * FROM categories WHERE id = ?";

        try {
            Connection connection = getConnection();

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
                    surveys.add(new Survey(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
                            resultSet.getTimestamp("created_at")));
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

        return new PagedEntity<>(page, totalPages, surveys);
    }

    @Override
    public void deleteSurvey(int id) throws DatabaseAccessException {
        String query = "DELETE FROM surveys WHERE id = ?";

        try {
            Connection connection = getConnection();
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

        try {
            Connection connection = getConnection();
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
