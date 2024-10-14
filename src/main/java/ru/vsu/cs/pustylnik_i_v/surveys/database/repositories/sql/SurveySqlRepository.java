package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SurveyRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.PostgresqlDataSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveySqlRepository extends BaseSqlRepository implements SurveyRepository {
    public SurveySqlRepository(PostgresqlDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Survey addSurvey(String name, String description, Integer categoryId, Date createdAt) {
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
            } catch (SQLException ignored) {
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public Survey getSurveyById(int id) throws SurveyNotFoundException {
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
            throw new SurveyNotFoundException(id);
        }
        throw new SurveyNotFoundException(id);
    }

    @Override
    public void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException {
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
        } catch (SQLException ignored) {}
    }

    @Override
    public List<Survey> getSurveys(Integer categoryId) throws CategoryNotFoundException {
        List<Survey> surveys = new ArrayList<>();

        String query = "SELECT * FROM surveys WHERE category_id = ?";
        String queryAll = "SELECT * FROM surveys";

        try {
            Connection connection = getConnection();
            PreparedStatement statement;
            if (categoryId != null) {
                statement = connection.prepareStatement(query);
                statement.setInt(1, categoryId);
            } else {
                statement = connection.prepareStatement(queryAll);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    surveys.add(new Survey(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("category_id"),
                            resultSet.getDate("created_at")));
                }
            }
        } catch (SQLException ignored) {
        }
        return surveys;
    }

    @Override
    public void deleteSurvey(int id) throws SurveyNotFoundException {
        String query = "DELETE FROM users WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.executeQuery();
        } catch (SQLException ignored) {
            throw new SurveyNotFoundException(id);
        }
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT COUNT(*) FROM surveys WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            if (statement.execute()) {
                return true;
            }

        } catch (SQLException ignored) {
        }
        return false;
    }
}
