package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;

public interface SessionRepository {
    public Session getSessionById(int id);

    public void addSession(Session s);

    public void updateSession(Session s);

    public void deleteSession(int id);

    public boolean exists(int id);
}
