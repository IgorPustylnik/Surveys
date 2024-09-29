package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.SessionRepository;

import java.util.Date;

public class SessionRepositoryMock implements SessionRepository {

    private final DBTableImitation<Session, Integer> sessions = new DBTableImitation<>(1000,
            params -> (new Session(0, (Integer) params[0], (Integer) params[1], (Date) params[2], (Date) params[3])),
            Session::getId);

    @Override
    public Session getSessionById(int id) {
        return sessions.get(id);
    }

    @Override
    public void addSession(int surveyId, Integer userId, Date startedAt, Date finishedAt) {
        sessions.add(surveyId, userId, startedAt, finishedAt);
    }

    @Override
    public void updateSession(Session s) {
        sessions.get(s.getId()).setSurveyId(s.getSurveyId());
        sessions.get(s.getId()).setUserId(s.getUserId());
        sessions.get(s.getId()).setStartedAt(s.getStartedAt());
        sessions.get(s.getId()).setFinishedAt(s.getFinishedAt());
    }

    @Override
    public void deleteSession(int id) {
        sessions.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return sessions.containsKey(id);
    }

}
