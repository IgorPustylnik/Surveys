package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;

import java.util.Date;
import java.util.List;

public interface SurveyRepository {
    void addSurvey(String name, String description, Integer categoryId, Date createdAt);

    Survey getSurveyById(int id);

    List<Survey> getSurveys(Integer categoryId);

    void updateSurvey(Survey s);

    void deleteSurvey(int id);

    boolean exists(int id);
}
