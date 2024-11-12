package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.QuestionStatsDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.OptionStats;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

import java.util.Map;

public class QuestionStatsMockDAO extends BaseMockDAO implements QuestionStatsDAO {

    public QuestionStatsMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Map<Integer, OptionStats> getQuestionStats(int surveyId) throws DatabaseAccessException {
        return Map.of();
    }
}
