package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.SurveyRepository;

import java.util.Date;
import java.util.List;

public class SurveyRepositoryMock implements SurveyRepository {

    private final DBTableImitation<Survey> surveys = new DBTableImitation<>(
            params -> (new Survey(0, (String) params[0], (String) params[1], (Integer) params[2], (Date) params[3])));

    @Override
    public Survey getSurveyById(int id) {
        return surveys.get(Survey::getId,id).get(0);
    }

    @Override
    public List<Survey> getSurveys(Integer categoryId) {
        if (categoryId == null) {
            return surveys.getAll();
        }
        return surveys.get(Survey::getCategoryId,categoryId);
    }

    @Override
    public void addSurvey(String name, String description, Integer categoryId, Date createdAt) {
        surveys.add(name, description, categoryId, createdAt);
    }

    @Override
    public void updateSurvey(Survey s) {
        surveys.get(Survey::getId,s.getId()).get(0).setName(s.getName());
        surveys.get(Survey::getId,s.getId()).get(0).setDescription(s.getDescription());
        surveys.get(Survey::getId,s.getId()).get(0).setCreatedAt(s.getCreatedAt());
    }

    @Override
    public void deleteSurvey(int id) {
        surveys.remove(Survey::getId,id);
    }

    @Override
    public boolean exists(int id) {
        return surveys.contains(Survey::getId,id);
    }

}
