package ru.vsu.cs.pustylnik_i_v.surveys.provider.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.sql.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.StatisticService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;

public class SqlServiceProvider implements ServiceProvider {

    private final UserService userService;
    private final SurveyService surveyService;
    private final SessionService sessionService;
    private final StatisticService statisticService;

    public SqlServiceProvider() {
        DatabaseSource dataSource = new DatabaseSource();

        AnswerDAO answerDAO = new AnswerSqlDAO(dataSource);
        CategoryDAO categoryDAO = new CategorySqlDAO(dataSource);
        QuestionDAO questionDAO = new QuestionSqlDAO(dataSource);
        SessionDAO sessionDAO = new SessionSqlDAO(dataSource);
        SessionQuestionDAO sessionQuestionDAO = new SessionQuestionSqlDAO(dataSource);
        SurveyDAO surveyDAO = new SurveySqlDAO(dataSource);
        UserDAO userDAO = new UserSqlDAO(dataSource);
        QuestionStatsDAO questionStatsDAO = new QuestionStatsSqlDAO(dataSource);


        this.userService = new UserService(userDAO);
        this.surveyService = new SurveyService(userDAO, surveyDAO, questionDAO, categoryDAO);
        this.sessionService = new SessionService(userDAO, answerDAO, sessionDAO, sessionQuestionDAO);
        this.statisticService = new StatisticService(questionStatsDAO);
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

    @Override
    public StatisticService getStatisticService() {
        return statisticService;
    }
}
