package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.QuestionStatsDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.base.BaseSqlDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.OptionStats;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QuestionStatsSqlDAO extends BaseSqlDAO implements QuestionStatsDAO {

    public QuestionStatsSqlDAO(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Map<Integer, OptionStats> getQuestionStats(int surveyId) throws DatabaseAccessException {
        String query = "SELECT q.id AS question_id, o.id AS option_id, " +
                "COUNT(a.option_id) AS selected_count " +
                "FROM questions q " +
                "JOIN options o ON q.id = o.question_id " +
                "LEFT JOIN answers a ON o.id = a.option_id " +
                "WHERE q.survey_id = ? " +
                "GROUP BY q.id, o.id " +
                "ORDER BY q.id, o.id";

        Map<Integer, OptionStats> questionStatsMap = new HashMap<>();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, surveyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                Map<Integer, Map<Integer, Integer>> optionCountsMap = new HashMap<>();
                Map<Integer, Integer> questionTotalCounts = new HashMap<>();

                while (resultSet.next()) {
                    int questionId = resultSet.getInt("question_id");
                    int optionId = resultSet.getInt("option_id");
                    int selectedCount = resultSet.getInt("selected_count");

                    optionCountsMap.computeIfAbsent(questionId, k -> new HashMap<>())
                            .put(optionId, selectedCount);

                    questionTotalCounts.put(questionId,
                            questionTotalCounts.getOrDefault(questionId, 0) + selectedCount);
                }

                for (Map.Entry<Integer, Map<Integer, Integer>> entry : optionCountsMap.entrySet()) {
                    int questionId = entry.getKey();
                    Map<Integer, Integer> optionCounts = entry.getValue();
                    int total = questionTotalCounts.getOrDefault(questionId, 0);
                    questionStatsMap.put(questionId, OptionStats.of(optionCounts, total));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e.getMessage());
        }

        return questionStatsMap;
    }



}
