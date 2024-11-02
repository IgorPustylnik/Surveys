package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.*;

import java.util.List;

public interface SurveysService {

    ServiceResponse<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ServiceResponse<?> deleteSurvey(Integer surveyId) throws DatabaseAccessException;

    ServiceResponse<Survey> addSurveyAndGetSelf(String name, String description, String categoryName, Integer authorId) throws DatabaseAccessException;

    ServiceResponse<?> setSurveyCategory(Integer surveyId, String categoryName) throws DatabaseAccessException;

    ServiceResponse<Integer> startSessionAndGetId(String userName, Integer surveyId) throws DatabaseAccessException;

    ServiceResponse<?> finishSession(Integer sessionId) throws DatabaseAccessException;

    ServiceResponse<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer page) throws DatabaseAccessException;

    ServiceResponse<List<Option>> getQuestionOptionList(Integer questionId) throws DatabaseAccessException;

    ServiceResponse<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ServiceResponse<?> submitAnswer (Integer sessionId, Integer optionId) throws DatabaseAccessException;

    ServiceResponse<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) throws DatabaseAccessException;

    ServiceResponse<?> deleteCategory(Integer categoryId) throws DatabaseAccessException;

    ServiceResponse<Survey> getSurvey(Integer surveyId) throws DatabaseAccessException;

}
