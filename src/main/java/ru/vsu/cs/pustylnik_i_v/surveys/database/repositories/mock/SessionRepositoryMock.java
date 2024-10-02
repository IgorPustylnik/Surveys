package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.SessionRepository;

import java.util.Date;

public class SessionRepositoryMock implements SessionRepository {

    private final DBTableImitation<Session> sessions = new DBTableImitation<>(
            params -> (new Session(0, (Integer) params[0], (Integer) params[1], (Date) params[2], (Date) params[3])));

    @Override
    public Session getSessionById(int id) {
        return sessions.get(Session::getId,id).get(0);
    }

    @Override
    public Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date finishedAt) {
        sessions.add(surveyId, userId, startedAt, finishedAt);
        return sessions.get(Session::getStartedAt, startedAt).get(0).getId();
    }

    @Override
    public void updateSession(Session s) {
        sessions.get(Session::getId,s.getId()).get(0).setSurveyId(s.getSurveyId());
        sessions.get(Session::getId,s.getId()).get(0).setUserId(s.getUserId());
        sessions.get(Session::getId,s.getId()).get(0).setStartedAt(s.getStartedAt());
        sessions.get(Session::getId,s.getId()).get(0).setFinishedAt(s.getFinishedAt());
    }

    @Override
    public void deleteSession(int id) {
        sessions.remove(Session::getId, id);
    }

    @Override
    public boolean exists(int id) {
        return sessions.contains(Session::getId,id);
    }

}
