package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.SessionRepository;

import java.util.HashMap;

public class SessionRepositoryMock implements SessionRepository {

    private HashMap<Integer, Session> sessions;

    @Override
    public Session getSessionById(int id) {
        return sessions.get(id);
    }

    @Override
    public void addSession(Session s) {
        sessions.put(s.getId(), s);
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
