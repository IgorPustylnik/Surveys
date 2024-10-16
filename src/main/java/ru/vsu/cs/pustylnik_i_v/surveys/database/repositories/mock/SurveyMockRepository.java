package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SurveyRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.RoleNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.util.Date;
import java.util.List;

public class SurveyMockRepository implements SurveyRepository {

    private final DBTableImitation<Survey> surveys = new DBTableImitation<>(
            params -> (new Survey(0, (String) params[0], (String) params[1], (Integer) params[2], (Date) params[3])));

    @Override
    public Survey getSurveyById(int id) {
        return surveys.get(Survey::getId,id).get(0);
    }

    @Override
    public void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException {
        List<Survey> query = surveys.get(Survey::getId, id);
        if (query.isEmpty()) {
            throw new SurveyNotFoundException(id);
        }
        query.get(0).setCategoryId(categoryId);
    }

    @Override
    public List<Survey> getSurveys(Integer categoryId) {
        if (categoryId == null) {
            return surveys.getAll();
        }
        return surveys.get(Survey::getCategoryId,categoryId);
    }

    @Override
    public Survey addSurvey(String name, String description, Integer categoryId, Date createdAt) {
        int id = surveys.add(name, description, categoryId, createdAt);
        return surveys.get(Survey::getId, id).get(0);
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
