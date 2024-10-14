package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.PostgresqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OptionSqlRepository extends BaseSqlRepository implements OptionRepository {
    public OptionSqlRepository(PostgresqlDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Option> getOptions(int questionId) {
        List<Option> options = new ArrayList<>();

        String query = "SELECT * FROM options WHERE question_id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, questionId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    options.add(new Option(resultSet.getInt("id"),
                            resultSet.getInt("question_id"),
                            resultSet.getString("description")));
                }
            }
        } catch (SQLException ignored) {}
        return options;
    }

    @Override
    public void addOption(int questionId, String description) {
        String checkQuestionQuery = "SELECT * FROM questions WHERE id = ?";

        String query = "INSERT INTO options (question_id, description) VALUES (?, ?)";

        try {
            Connection connection = getConnection();

            PreparedStatement checkQuestionStatement = connection.prepareStatement(checkQuestionQuery);
            checkQuestionStatement.setInt(1, questionId);

            if (!checkQuestionStatement.execute()) {
                return;
            }

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, questionId);
            statement.setString(2, description);

            statement.execute();
        } catch (SQLException ignored) {
            System.out.println("Error adding option");
        }
    }
}
