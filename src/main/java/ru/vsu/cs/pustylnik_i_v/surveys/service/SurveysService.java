package ru.vsu.cs.pustylnik_i_v.surveys.service;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.*;

import java.util.List;

public interface SurveysService {

    // User info

    ResponseEntity<AuthBody> login(String name, String password);

    ResponseEntity<AuthBody> register(String name, String password);

    ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password);

    ResponseEntity<?> changePassword(String name, String newPassword);

    ResponseEntity<?> addAdmin(String userName, String email);

    // Surveys

    ResponseEntity<PagedEntity<List<Survey>>> getSurveysPagedList(Integer categoryId, Integer page);

    ResponseEntity<?> deleteSurvey(Integer surveyId);

    ResponseEntity<?> addSurvey(String name, String description, String categoryName);

    ResponseEntity<PagedEntity<Question>> getQuestionPagedEntity(Integer surveyId, Integer page);

}
