package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.*;

import java.util.List;

public interface SurveysService {

    ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page);

    ResponseEntity<?> deleteSurvey(Integer surveyId);

    ResponseEntity<Integer> addSurveyAndGetId(String name, String description, String categoryName);

    ResponseEntity<Integer> startSessionAndGetId(String userName, Integer surveyId);

    ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer page);

    ResponseEntity<List<Option>> getQuestionOptionList(Integer questionId);

    ResponseEntity<?> submitAnswer (Integer sessionId, Integer optionId);

}
