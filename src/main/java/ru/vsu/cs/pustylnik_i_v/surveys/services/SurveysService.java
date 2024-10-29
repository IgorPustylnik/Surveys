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

    ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ResponseEntity<?> deleteSurvey(Integer surveyId) throws DatabaseAccessException;

    ResponseEntity<Survey> addSurveyAndGetSelf(String name, String description, String categoryName) throws DatabaseAccessException;

    ResponseEntity<?> setSurveyCategory(Integer surveyId, String categoryName) throws DatabaseAccessException;

    ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId) throws DatabaseAccessException;

    ResponseEntity<?> finishSession(Integer sessionId) throws DatabaseAccessException;

    ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer page) throws DatabaseAccessException;

    ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId) throws DatabaseAccessException;

    ResponseEntity<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ResponseEntity<?> submitAnswer (Integer sessionId, Integer optionId) throws DatabaseAccessException;

    ResponseEntity<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType) throws DatabaseAccessException;

    ResponseEntity<String> getCategoryName(Integer categoryId) throws DatabaseAccessException;

    ResponseEntity<?> deleteCategory(Integer categoryId) throws DatabaseAccessException;

}
