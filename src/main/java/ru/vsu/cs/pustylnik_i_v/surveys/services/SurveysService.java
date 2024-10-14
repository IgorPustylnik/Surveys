package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.*;

import java.util.List;

public interface SurveysService {

    ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page);

    ResponseEntity<?> deleteSurvey(Integer surveyId);

    ResponseEntity<Survey> addSurveyAndGetSelf(String name, String description, String categoryName);

    ResponseEntity<?> setSurveyCategory(Integer surveyId, String categoryName);

    ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId);

    ResponseEntity<?> finishSession(Integer sessionId);

    ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer page);

    ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId);

    ResponseEntity<PagedEntity<List<Category>>> getCategoriesPagedList(Integer page);

    ResponseEntity<?> submitAnswer (Integer sessionId, Integer optionId);

    ResponseEntity<?> addQuestionToSurvey(Integer surveyId, String description, List<String> options, QuestionType questionType);

    ResponseEntity<String> getCategoryName(Integer categoryId);

    ResponseEntity<?> deleteCategory(Integer categoryId);

}
