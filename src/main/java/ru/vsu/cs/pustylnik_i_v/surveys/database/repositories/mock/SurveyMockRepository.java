package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SurveyRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.Date;
import java.util.List;

public class SurveyMockRepository extends BaseMockRepository implements SurveyRepository {
    public SurveyMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Survey getSurveyById(int id) {
        return database.surveys.get(Survey::getId, id).get(0);
    }

    @Override
    public void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException {
        List<Survey> query = database.surveys.get(Survey::getId, id);
        if (query.isEmpty()) {
            throw new SurveyNotFoundException(id);
        }
        query.get(0).setCategoryId(categoryId);
    }

    @Override
    public PagedEntity<List<Survey>> getSurveysPagedEntity(Integer categoryId, Integer page, Integer perPageAmount) {
        List<Survey> filtered;
        int fromIndex = perPageAmount * page;
        if (categoryId == null) {
            filtered = database.surveys.getAll();
        } else {
            filtered = database.surveys.get(Survey::getCategoryId, categoryId);
        }
        int toIndex = Math.min(fromIndex + perPageAmount, filtered.size());
        List<Survey> sublist = filtered.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) database.surveys.size() / perPageAmount);
        if (totalPages < 1) totalPages = 1;
        return new PagedEntity<>(page, totalPages, sublist);
    }

    @Override
    public Survey addSurvey(String name, String description, Integer categoryId, String authorName, Date createdAt) {
        List<Category> query = database.categories.get(Category::getId, categoryId);
        String categoryName = query.get(0).getName();
        Integer authorId = database.users.get(User::getName, authorName).get(0).getId();
        int id = database.surveys.add(name, description, categoryId, categoryName, authorId, createdAt);
        return database.surveys.get(Survey::getId, id).get(0);
    }

    @Override
    public void deleteSurvey(int id) {
        database.surveys.remove(Survey::getId, id);
    }

    @Override
    public boolean exists(int id) {
        return database.surveys.contains(Survey::getId, id);
    }

}
