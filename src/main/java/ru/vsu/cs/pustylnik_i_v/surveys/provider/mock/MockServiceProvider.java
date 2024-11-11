package ru.vsu.cs.pustylnik_i_v.surveys.provider.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;

import java.util.List;

public class MockServiceProvider implements ServiceProvider {

    private final UserService userService;
    private final SurveyService surveyService;
    private final SessionService sessionService;

    public MockServiceProvider() {
        MockDatabaseSource db = new MockDatabaseSource();
        AnswerDAO answerDAO = new AnswerMockDAO(db);
        CategoryDAO categoryDAO = new CategoryMockDAO(db);
        QuestionDAO questionDAO = new QuestionMockDAO(db);
        SessionDAO sessionDAO = new SessionMockDAO(db);
        SessionQuestionDAO sessionQuestionDAO = new SessionQuestionMockDAO(db);
        SurveyDAO surveyDAO = new SurveyMockDAO(db);
        UserDAO userDAO = new UserMockDAO(db);

        this.userService = new UserService(userDAO);
        this.surveyService = new SurveyService(userDAO, surveyDAO, questionDAO,
                answerDAO, categoryDAO, sessionDAO);
        this.sessionService = new SessionService(userDAO, answerDAO, sessionDAO, sessionQuestionDAO);

        addMockData();
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public SurveyService getSurveyService() {
        return surveyService;
    }

    @Override
    public SessionService getSessionService() {
        return sessionService;
    }

    private void addMockData() {
        try {
            userService.register("admin", "Admin123");
            userService.setRole("admin", RoleType.ADMIN);

            ServiceResponse<Survey> serviceResponse = surveyService.addSurveyAndGetSelf(
                    "First",
                    "test description",
                    "Science",
                    1);
            Survey survey = serviceResponse.body();
            surveyService.addQuestionToSurvey(survey.getId(), "What year is it?", List.of(
                    "2015", "2007", "2024", "2020"
            ), QuestionType.SINGLE_CHOICE);
            surveyService.addQuestionToSurvey(survey.getId(), "Choose anything", List.of(
                    "Apples", "Oranges", "Melons"
            ), QuestionType.MULTIPLE_CHOICE);
        } catch (DatabaseAccessException ignored) {
        }
    }

}
