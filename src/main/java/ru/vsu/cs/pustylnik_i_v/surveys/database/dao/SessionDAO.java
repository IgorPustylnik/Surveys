package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.util.Date;

public interface SessionDAO {
    Session getSessionById(int id) throws SessionNotFoundException, DatabaseAccessException;

    Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date endedAt) throws SurveyNotFoundException, DatabaseAccessException, UserNotFoundException;

    void updateSession(Session s) throws SessionNotFoundException, DatabaseAccessException;

    boolean exists(int id) throws DatabaseAccessException;
}
