package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.QuestionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionSqlDAO extends BaseSqlDAO implements QuestionDAO {
    public QuestionSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Question getQuestion(int id) throws QuestionNotFoundException, DatabaseAccessException {
        String query = "SELECT q.id AS question_id, q.survey_id, q.text, q.type, o.id AS option_id, o.description AS option_description " +
                "FROM questions q " +
                "LEFT JOIN options o ON q.id = o.question_id " +
                "WHERE q.id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int questionId = resultSet.getInt("question_id");
                    int surveyId = resultSet.getInt("survey_id");
                    String questionText = resultSet.getString("text");
                    String questionTypeString = resultSet.getString("type");
                    QuestionType questionType = questionTypeString != null ? QuestionType.valueOf(questionTypeString.toUpperCase()) : null;

                    Question question = new Question(
                            id,
                            surveyId,
                            questionText,
                            questionType,
                            new ArrayList<>()
                    );

                    int optionId = resultSet.getInt("option_id");
                    if (optionId > 0) {
                        Option option = new Option(
                                optionId,
                                questionId,
                                resultSet.getString("option_description")
                        );
                        question.getOptions().add(option);
                    }
                    return question;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
        throw new QuestionNotFoundException(id);
    }

    @Override
    public List<Question> getQuestions(int surveyId) throws DatabaseAccessException {
        Map<Integer, Question> questionsMap = new HashMap<>();

        String query = "SELECT q.id AS question_id, q.survey_id, q.text, q.type, o.id AS option_id, o.description AS option_description " +
                "FROM questions q " +
                "LEFT JOIN options o ON q.id = o.question_id " +
                "WHERE q.survey_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, surveyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int questionId = resultSet.getInt("question_id");
                    String questionText = resultSet.getString("text");
                    String questionTypeString = resultSet.getString("type");
                    QuestionType questionType = questionTypeString != null ? QuestionType.valueOf(questionTypeString.toUpperCase()) : null;

                    Question question = questionsMap.computeIfAbsent(questionId, id ->
                            new Question(
                                    id,
                                    surveyId,
                                    questionText,
                                    questionType,
                                    new ArrayList<>()
                            )
                    );

                    int optionId = resultSet.getInt("option_id");
                    if (optionId > 0) {
                        Option option = new Option(
                                optionId,
                                questionId,
                                resultSet.getString("option_description")
                        );
                        question.getOptions().add(option);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }

        return new ArrayList<>(questionsMap.values());
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type, List<String> options) throws SurveyNotFoundException, DatabaseAccessException {
        String queryCheckSurvey = "SELECT id FROM surveys WHERE id = ?";
        String queryQuestion = "INSERT INTO questions (survey_id, text, type) VALUES (?, ?, ?) RETURNING id";
        String queryOptions = "INSERT INTO options (question_id, description) VALUES (?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement statementCheckSurvey = connection.prepareStatement(queryCheckSurvey);
            statementCheckSurvey.setInt(1, surveyId);

            try (ResultSet resultSet = statementCheckSurvey.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SurveyNotFoundException(surveyId);
                }
            }

            PreparedStatement statementQuestion = connection.prepareStatement(queryQuestion);

            statementQuestion.setInt(1, surveyId);
            statementQuestion.setString(2, text);
            statementQuestion.setObject(3, type.toString().toLowerCase(), Types.OTHER);

            Integer questionId = null;

            try (ResultSet resultSet = statementQuestion.executeQuery()) {
                if (resultSet.next()) {
                    questionId = resultSet.getInt("id");
                }
            }

            if (questionId == null) {
                throw new DatabaseAccessException("Failed to add question");
            }

            PreparedStatement statementOption;

            for (String option : options) {
                statementOption = connection.prepareStatement(queryOptions);
                statementOption.setInt(1, questionId);
                statementOption.setString(2, option);
                statementOption.execute();
            }

        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void updateQuestion(Question question) throws QuestionNotFoundException, DatabaseAccessException {
        String checkQuery = "SELECT COUNT(*) FROM questions WHERE id = ?";
        String updateQuery = "UPDATE questions SET (survey_id, text, type) = (?, ?, ?) WHERE id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, question.getId());
                try {
                    if (!checkStatement.execute()) {
                        throw new QuestionNotFoundException(question.getId());
                    }
                } catch (SQLException e) {
                    throw new QuestionNotFoundException(question.getId());
                }
            }

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, question.getSurveyId());
                updateStatement.setString(2, question.getText());
                updateStatement.setObject(3, question.getType().toString().toLowerCase(), Types.OTHER);
                updateStatement.setInt(4, question.getId());

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteQuestion(int id) throws DatabaseAccessException {
        String query = "DELETE FROM questions WHERE id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }
    }
}
