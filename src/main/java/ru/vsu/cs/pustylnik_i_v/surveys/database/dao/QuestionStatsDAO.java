package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.OptionStats;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

import javax.xml.crypto.Data;
import java.util.Map;

public interface QuestionStatsDAO {

    public Map<Integer, OptionStats> getQuestionStats(int surveyId) throws DatabaseAccessException;

}
