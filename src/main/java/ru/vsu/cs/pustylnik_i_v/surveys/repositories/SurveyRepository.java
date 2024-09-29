package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Survey;

public interface SurveyRepository {
    public Survey getSurveyById(int id);

    public void addSurvey(Survey s);

    public void updateSurvey(Survey s);

    public void deleteSurvey(int id);

    public boolean exists(int id);
}
