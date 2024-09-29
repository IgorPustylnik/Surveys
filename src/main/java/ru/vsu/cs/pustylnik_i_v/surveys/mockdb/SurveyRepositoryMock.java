package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.SurveyRepository;

import java.util.HashMap;

public class SurveyRepositoryMock implements SurveyRepository {

    private HashMap<Integer, Survey> surveys;

    @Override
    public Survey getSurveyById(int id) {
        return surveys.get(id);
    }

    @Override
    public void addSurvey(Survey s) {
        surveys.put(s.getId(), s);
    }

    @Override
    public void updateSurvey(Survey s) {
        surveys.get(s.getId()).setName(s.getName());
        surveys.get(s.getId()).setDescription(s.getDescription());
        surveys.get(s.getId()).setCreatedAt(s.getCreatedAt());
    }

    @Override
    public void deleteSurvey(int id) {
        surveys.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return surveys.containsKey(id);
    }

}
