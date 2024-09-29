package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Survey;

import java.util.Date;

public interface SurveyRepository {
    Survey getSurveyById(int id);

    void addSurvey(String name, String description, Integer categoryId, Date createdAt);

    void updateSurvey(Survey s);

    void deleteSurvey(int id);

    boolean exists(int id);
}
