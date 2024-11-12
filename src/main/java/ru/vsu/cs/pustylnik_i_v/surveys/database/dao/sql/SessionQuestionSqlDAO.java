package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SessionQuestionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.SessionOption;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.SessionQuestion;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionQuestionSqlDAO extends BaseSqlDAO implements SessionQuestionDAO {

    public SessionQuestionSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<SessionQuestion> getQuestions(int sessionId) throws SessionNotFoundException, DatabaseAccessException {
        String query = "SELECT ss.id as session_id, q.id AS question_id, q.text AS text, q.type AS type, " +
                "o.id AS option_id, o.description AS option_description, " +
                "CASE WHEN a.option_id IS NOT NULL THEN TRUE ELSE FALSE END AS selected " +
                "FROM questions q " +
                "JOIN options o ON q.id = o.question_id " +
                "JOIN surveys sv ON q.survey_id = sv.id " +
                "JOIN sessions ss ON sv.id = ss.survey_id " +
                "LEFT JOIN answers a ON ss.id = a.session_id AND a.option_id = o.id " +
                "WHERE ss.id = ?";

        List<SessionQuestion> questions = new ArrayList<>();
        Map<Integer, SessionQuestion> questionMap = new HashMap<>();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sessionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int questionId = resultSet.getInt("question_id");
                    String text = resultSet.getString("text");
                    QuestionType type = QuestionType.valueOf(resultSet.getString("type").toUpperCase());
                    int optionId = resultSet.getInt("option_id");
                    String optionDescription = resultSet.getString("option_description");
                    boolean selected = resultSet.getBoolean("selected");

                    SessionQuestion question = questionMap.get(questionId);
                    if (question == null) {
                        question = new SessionQuestion(questionId, sessionId, text, type, new ArrayList<>());
                        questionMap.put(questionId, question);
                        questions.add(question);
                    }

                    SessionOption option = new SessionOption(optionId, questionId, optionDescription, selected);
                    question.getOptions().add(option);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }

        if (questions.isEmpty()) {
            throw new SessionNotFoundException(sessionId);
        }

        return questions;
    }

}
