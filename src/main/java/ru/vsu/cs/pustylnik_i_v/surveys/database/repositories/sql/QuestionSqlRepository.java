package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.QuestionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.PostgresqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionSqlRepository extends BaseSqlRepository implements QuestionRepository {
    public QuestionSqlRepository(PostgresqlDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Question> getQuestions(Integer surveyId) {
        List<Question> questions = new ArrayList<>();

        String query = "SELECT * FROM questions WHERE survey_id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, surveyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    questions.add(new Question(resultSet.getInt("id"),
                            resultSet.getInt("id"),
                            resultSet.getString("text"),
                            QuestionType.valueOf(resultSet.getString("type").toUpperCase())));
                }
            }
        } catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        return questions;
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type) {
        String query = "INSERT INTO questions (survey_id, text, type) VALUES (?, ?, ?)";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, surveyId);
            statement.setString(2, text);
            statement.setObject(3, type.toString().toLowerCase(), java.sql.Types.OTHER);

            statement.executeQuery();
        } catch (SQLException ignored) {}
    }
}
