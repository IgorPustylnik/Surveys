package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

public interface SessionRepository {
    public Session getSessionById(int id);

    public void addSession(Session s) throws SurveyNotFoundException, UserNotFoundException;

    public void updateSession(Session s);

    public void deleteSession(int id);

    public boolean exists(int id);
}
