package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Survey;

public interface SurveyRepository {
    Survey getSurveyById(int id);

    void addSurvey(Survey s);

    void updateSurvey(Survey s);

    void deleteSurvey(int id);

    boolean exists(int id);
}
