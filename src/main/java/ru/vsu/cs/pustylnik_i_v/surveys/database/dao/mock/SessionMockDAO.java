package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SessionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionMockDAO extends BaseMockDAO implements SessionDAO {
    public SessionMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Session getSessionById(int id) {
        List<DBTableSimulationFilter<Session>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        return database.sessions.get(filters).get(0);
    }

    @Override
    public Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date finishedAt) throws UserNotFoundException, SurveyNotFoundException {
        List<DBTableSimulationFilter<Survey>> filtersSurvey = new ArrayList<>();
        filtersSurvey.add(DBTableSimulationFilter.of(s -> s.getId() == surveyId));

        List<Survey> query = database.surveys.get(filtersSurvey);
        if (query.isEmpty()) {
            throw new SurveyNotFoundException(surveyId);
        }

        String surveyName = query.get(0).getName();

        List<DBTableSimulationFilter<User>> filtersUser = new ArrayList<>();
        filtersSurvey.add(DBTableSimulationFilter.of(u -> u.getId() == userId));

        List<User> queryUser = database.users.get(filtersUser);
        if (queryUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        database.sessions.add(surveyId, surveyName, userId, startedAt, finishedAt);

        List<DBTableSimulationFilter<Session>> filtersSession = new ArrayList<>();
        filtersSession.add(DBTableSimulationFilter.of(s -> s.getStartedAt() == startedAt));

        return database.sessions.get(filtersSession).get(0).getId();
    }

    @Override
    public void updateSession(Session s) throws SessionNotFoundException {
        List<DBTableSimulationFilter<Session>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(session -> session.getId() == s.getId()));

        List<Session> query = database.sessions.get(filters);

        if (query.isEmpty()) {
            throw new SessionNotFoundException(s.getId());
        }

        Session session = query.get(0);

        session.setSurveyId(s.getSurveyId());
        session.setSurveyName(s.getSurveyName());
        session.setUserId(s.getUserId());
        session.setStartedAt(s.getStartedAt());
        session.setFinishedAt(s.getFinishedAt());
    }

    @Override
    public boolean exists(int id) {
        List<DBTableSimulationFilter<Session>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        return database.sessions.contains(filters);
    }

}
