package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import java.util.Date;

public interface SessionRepository {
    Session getSessionById(int id) throws SessionNotFoundException, DatabaseAccessException;

    Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date endedAt) throws DatabaseAccessException;

    void updateSession(Session s) throws SessionNotFoundException, DatabaseAccessException;

    boolean exists(int id) throws DatabaseAccessException;
}
