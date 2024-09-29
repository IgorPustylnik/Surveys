package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.SurveyRepository;

import java.util.Date;

public class SurveyRepositoryMock implements SurveyRepository {

    private final DBTableImitation<Survey, Integer> surveys = new DBTableImitation<>(1000,
            params -> (new Survey(0, (String) params[0], (String) params[1], (Integer) params[2], (Date) params[3])),
            Survey::getId);

    @Override
    public Survey getSurveyById(int id) {
        return surveys.get(id);
    }

    @Override
    public void addSurvey(String name, String description, Integer categoryId, Date createdAt) {
        surveys.add(name, description, categoryId, createdAt);
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
