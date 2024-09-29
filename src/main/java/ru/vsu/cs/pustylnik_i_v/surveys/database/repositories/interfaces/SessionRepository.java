package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;

import java.util.Date;

public interface SessionRepository {
    Session getSessionById(int id);

    void addSession(int surveyId, Integer userId, Date startedAt, Date endedAt);

    void updateSession(Session s);

    void deleteSession(int id);

    boolean exists(int id);
}
