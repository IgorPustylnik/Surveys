package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.Date;
import java.util.List;

public interface SurveyRepository {
    Survey addSurvey(String name, String description, Integer categoryId, String authorName, Date createdAt) throws DatabaseAccessException;

    Survey getSurveyById(int id) throws SurveyNotFoundException, DatabaseAccessException;

    void updateSurveyCategoryName(int id, Integer categoryId) throws SurveyNotFoundException, DatabaseAccessException;

    PagedEntity<List<Survey>> getSurveysPagedEntity(Integer categoryId, Date fromDate, Date toDate, Integer page, Integer perPageAmount) throws CategoryNotFoundException, DatabaseAccessException;

    void deleteSurvey(int id) throws DatabaseAccessException;

    boolean exists(int id) throws DatabaseAccessException;
}
