package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;

public interface SessionRepository {
    Session getSessionById(int id);

    void addSession(Session s);

    void updateSession(Session s);

    void deleteSession(int id);

    boolean exists(int id);
}
