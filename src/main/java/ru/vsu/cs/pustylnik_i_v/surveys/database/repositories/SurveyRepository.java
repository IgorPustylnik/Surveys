package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.util.Date;
import java.util.List;

public interface SurveyRepository {
    Survey addSurvey(String name, String description, Integer categoryId, Date createdAt);

    Survey getSurveyById(int id) throws SurveyNotFoundException;

    void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException;

    List<Survey> getSurveys(Integer categoryId) throws CategoryNotFoundException;

    void deleteSurvey(int id) throws SurveyNotFoundException;

    boolean exists(int id);
}
