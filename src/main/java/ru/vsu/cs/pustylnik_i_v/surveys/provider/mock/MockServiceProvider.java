package ru.vsu.cs.pustylnik_i_v.surveys.provider.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.SurveysServiceImpl;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.UserInfoServiceImpl;

import java.util.List;

public class MockServiceProvider implements ServiceProvider {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;

    public MockServiceProvider() {
        MockDatabaseSource db = new MockDatabaseSource();
        AnswerRepository answerRepository = new AnswerMockRepository(db);
        CategoryRepository categoryRepository = new CategoryMockRepository(db);
        OptionRepository optionRepository = new OptionMockRepository(db);
        QuestionRepository questionRepository = new QuestionMockRepository(db);
        SessionRepository sessionRepository = new SessionMockRepository(db);
        SurveyRepository surveyRepository = new SurveyMockRepository(db);
        UserRepository userRepository = new UserMockRepository(db);

        this.userInfoService = new UserInfoServiceImpl(userRepository);
        this.surveysService = new SurveysServiceImpl(userRepository, surveyRepository, questionRepository,
                optionRepository, answerRepository, categoryRepository, sessionRepository);

        addMockData();
    }

    @Override
    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    @Override
    public SurveysService getSurveysService() {
        return surveysService;
    }

    private void addMockData() {
        try {
            userInfoService.register("admin", "admin");
            userInfoService.setRole("admin", RoleType.ADMIN);

            ServiceResponse<Survey> serviceResponse = surveysService.addSurveyAndGetSelf(
                    "First",
                    "test description",
                    "Science",
                    1);
            Survey survey = serviceResponse.body();
            surveysService.addQuestionToSurvey(survey.getId(), "What year is it?", List.of(
                    "2015", "2007", "2024", "2020"
            ), QuestionType.SINGLE_CHOICE);
            surveysService.addQuestionToSurvey(survey.getId(), "Choose anything", List.of(
                    "Apples", "Oranges", "Melons"
            ), QuestionType.MULTIPLE_CHOICE);
        } catch (DatabaseAccessException ignored) {
        }
    }

}
