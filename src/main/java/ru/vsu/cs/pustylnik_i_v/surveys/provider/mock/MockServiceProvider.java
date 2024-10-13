package ru.vsu.cs.pustylnik_i_v.surveys.provider.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.*;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.SurveysServiceImpl;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.UserInfoServiceImpl;

public class MockServiceProvider implements ServiceProvider {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;

    public MockServiceProvider() {
        AnswerRepository answerRepository = new AnswerMockRepository();
        CategoryRepository categoryRepository = new CategoryMockRepository();
        OptionRepository optionRepository = new OptionMockRepository();
        QuestionRepository questionRepository = new QuestionMockRepository();
        RoleRepository roleRepository = new RoleMockRepository();
        SessionRepository sessionRepository = new SessionMockRepository();
        SurveyRepository surveyRepository = new SurveyMockRepository();
        UserRepository userRepository = new UserMockRepository();

        this.userInfoService = new UserInfoServiceImpl(userRepository, roleRepository);
        this.surveysService = new SurveysServiceImpl(userRepository, surveyRepository, questionRepository,
                optionRepository, answerRepository, categoryRepository, sessionRepository);
    }

    @Override
    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    @Override
    public SurveysService getSurveysService() {
        return surveysService;
    }
}
