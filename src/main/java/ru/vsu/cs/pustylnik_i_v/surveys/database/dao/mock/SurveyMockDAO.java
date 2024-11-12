package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SurveyDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyMockDAO extends BaseMockDAO implements SurveyDAO {
    public SurveyMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Survey getSurveyById(int id) {
        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        return database.surveys.get(filters).get(0);
    }

    @Override
    public void updateSurveyCategory(int id, Integer categoryId) throws SurveyNotFoundException {
        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        List<Survey> query = database.surveys.get(filters);

        if (query.isEmpty()) {
            throw new SurveyNotFoundException(id);
        }

        query.get(0).setCategoryId(categoryId);
    }

    @Override
    public void updateSurvey(Survey survey) throws SurveyNotFoundException {
        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == survey.getId()));

        List<Survey> query = database.surveys.get(filters);

        if (query.isEmpty()) {
            throw new SurveyNotFoundException(survey.getId());
        }

        Survey surveyDb = query.get(0);
        surveyDb.setName(survey.getName());
        surveyDb.setDescription(survey.getDescription());
        surveyDb.setCategoryId(survey.getCategoryId());
        surveyDb.setCategoryName(survey.getCategoryName());
        surveyDb.setQuestionsAmount(survey.getQuestionsAmount());
    }

    @Override
    public PagedEntity<List<Survey>> getSurveysPagedEntity(String authorName, Integer categoryId, Date fromDate, Date toDate, int page, int perPageAmount) {
        List<Survey> filtered;
        int fromIndex = perPageAmount * page;
        if (categoryId == null && fromDate == null && toDate == null) {
            filtered = database.surveys.getAll();
        } else {
            List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
            if (authorName != null) {
                filters.add(DBTableSimulationFilter.of(survey -> survey.getAuthorName().equals(authorName)));
            }
            if (categoryId != null) {
                filters.add(DBTableSimulationFilter.of(survey -> survey.getCategoryId().equals(categoryId)));
            }
            if (fromDate != null) {
                filters.add(DBTableSimulationFilter.of(survey -> survey.getCreatedAt().compareTo(fromDate) >= 0));
            }
            if (toDate != null) {
                filters.add(DBTableSimulationFilter.of(survey -> survey.getCreatedAt().compareTo(toDate) <= 0));
            }
            filtered = database.surveys.get(filters);
        }
        int toIndex = Math.min(fromIndex + perPageAmount, filtered.size());
        List<Survey> sublist = filtered.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) database.surveys.size() / perPageAmount);
        if (totalPages < 1) totalPages = 1;
        return new PagedEntity<>(page, totalPages, sublist);
    }

    @Override
    public Survey addSurvey(String name, String description, Integer categoryId, String authorName, Date createdAt) {
        List<DBTableSimulationFilter<Category>> filtersCategory = new ArrayList<>();
        filtersCategory.add(DBTableSimulationFilter.of(s -> s.getId() == categoryId));

        List<Category> query = database.categories.get(filtersCategory);

        String categoryName = query.get(0).getName();

        int id = database.surveys.add(name, description, categoryId, categoryName, authorName, 0, createdAt);

        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        return database.surveys.get(filters).get(0);
    }

    @Override
    public void deleteSurvey(int id) {
        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        database.surveys.remove(filters);
    }

    @Override
    public boolean exists(int id) {
        List<DBTableSimulationFilter<Survey>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        return database.surveys.contains(filters);
    }

}
