package ru.vsu.cs.pustylnik_i_v.surveys.provider.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;

public class SqlServiceProvider implements ServiceProvider {

    private final UserService userService;
    private final SurveyService surveyService;
    private final SessionService sessionService;

    public SqlServiceProvider() {
        DatabaseSource dataSource = new DatabaseSource();

        AnswerRepository answerRepository = new AnswerSqlRepository(dataSource);
        CategoryRepository categoryRepository = new CategorySqlRepository(dataSource);
        QuestionRepository questionRepository = new QuestionSqlRepository(dataSource);
        SessionRepository sessionRepository = new SessionSqlRepository(dataSource);
        SurveyRepository surveyRepository = new SurveySqlRepository(dataSource);
        UserRepository userRepository = new UserSqlRepository(dataSource);

        this.userService = new UserService(userRepository);
        this.surveyService = new SurveyService(userRepository, surveyRepository, questionRepository,
                answerRepository, categoryRepository, sessionRepository);
        this.sessionService = new SessionService(userRepository, answerRepository, sessionRepository);
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
}
