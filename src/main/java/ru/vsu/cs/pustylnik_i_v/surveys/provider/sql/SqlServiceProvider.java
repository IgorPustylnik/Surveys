package ru.vsu.cs.pustylnik_i_v.surveys.provider.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.TokenValidationService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.AESCryptoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.SurveysServiceImpl;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.TokenValidationServiceImpl;
import ru.vsu.cs.pustylnik_i_v.surveys.services.impl.UserInfoServiceImpl;

public class SqlServiceProvider implements ServiceProvider {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;
    private final TokenValidationService tokenValidationService;

    public SqlServiceProvider() {
        DatabaseSource dataSource = new DatabaseSource();

        AnswerRepository answerRepository = new AnswerSqlRepository(dataSource);
        CategoryRepository categoryRepository = new CategorySqlRepository(dataSource);
        OptionRepository optionRepository = new OptionSqlRepository(dataSource);
        QuestionRepository questionRepository = new QuestionSqlRepository(dataSource);
        RoleRepository roleRepository = new RoleSqlRepository(dataSource);
        SessionRepository sessionRepository = new SessionSqlRepository(dataSource);
        SurveyRepository surveyRepository = new SurveySqlRepository(dataSource);
        UserRepository userRepository = new UserSqlRepository(dataSource);

        this.userInfoService = new UserInfoServiceImpl(userRepository, roleRepository);
        this.surveysService = new SurveysServiceImpl(userRepository, surveyRepository, questionRepository,
                optionRepository, answerRepository, categoryRepository, sessionRepository);
        this.tokenValidationService = new TokenValidationServiceImpl(AESCryptoService.getInstance(), userInfoService);
    }

    @Override
    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    @Override
    public SurveysService getSurveysService() {
        return surveysService;
    }

    @Override
    public TokenValidationService getTokenValidationService() {
        return tokenValidationService;
    }
}
