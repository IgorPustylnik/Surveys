package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.OptionStats;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import java.util.Map;

public class StatisticService {

    private final QuestionStatsDAO questionStatsDAO;

    public StatisticService(QuestionStatsDAO questionStatsDAO) {
        this.questionStatsDAO = questionStatsDAO;
    }

    public ServiceResponse<Map<Integer, OptionStats>> getSurveyStats(int surveyId) throws DatabaseAccessException {
        return new ServiceResponse<>(true, "Statistics successfully found", questionStatsDAO.getQuestionStats(surveyId));
    }

}
