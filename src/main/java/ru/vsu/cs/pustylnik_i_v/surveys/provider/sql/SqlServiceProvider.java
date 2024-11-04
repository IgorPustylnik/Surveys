package ru.vsu.cs.pustylnik_i_v.surveys.provider.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;

public class SqlServiceProvider implements ServiceProvider {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;

    public SqlServiceProvider() {
        DatabaseSource dataSource = new DatabaseSource();

        AnswerRepository answerRepository = new AnswerSqlRepository(dataSource);
        CategoryRepository categoryRepository = new CategorySqlRepository(dataSource);
        OptionRepository optionRepository = new OptionSqlRepository(dataSource);
        QuestionRepository questionRepository = new QuestionSqlRepository(dataSource);
        SessionRepository sessionRepository = new SessionSqlRepository(dataSource);
        SurveyRepository surveyRepository = new SurveySqlRepository(dataSource);
        UserRepository userRepository = new UserSqlRepository(dataSource);

        this.userInfoService = new UserInfoService(userRepository);
        this.surveysService = new SurveysService(userRepository, surveyRepository, questionRepository,
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
