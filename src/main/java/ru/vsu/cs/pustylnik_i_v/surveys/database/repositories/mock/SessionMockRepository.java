package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SessionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;

import java.util.Date;

public class SessionMockRepository extends BaseMockRepository implements SessionRepository {
    public SessionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Session getSessionById(int id) {
        return database.sessions.get(Session::getId,id).get(0);
    }

    @Override
    public Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date finishedAt) {
        database.sessions.add(surveyId, userId, startedAt, finishedAt);
        return database.sessions.get(Session::getStartedAt, startedAt).get(0).getId();
    }

    @Override
    public void updateSession(Session s) {
        database.sessions.get(Session::getId,s.getId()).get(0).setSurveyId(s.getSurveyId());
        database.sessions.get(Session::getId,s.getId()).get(0).setUserId(s.getUserId());
        database.sessions.get(Session::getId,s.getId()).get(0).setStartedAt(s.getStartedAt());
        database.sessions.get(Session::getId,s.getId()).get(0).setFinishedAt(s.getFinishedAt());
    }

    @Override
    public boolean exists(int id) {
        return database.sessions.contains(Session::getId,id);
    }

}
