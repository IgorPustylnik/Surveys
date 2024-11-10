package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.util.List;

public interface QuestionDAO {
    List<Question> getQuestions(int surveyId) throws SurveyNotFoundException, DatabaseAccessException;

    void addQuestion(int surveyId, String text, QuestionType type, List<String> options) throws SurveyNotFoundException, DatabaseAccessException;

    void deleteQuestion(int id) throws DatabaseAccessException;
}
