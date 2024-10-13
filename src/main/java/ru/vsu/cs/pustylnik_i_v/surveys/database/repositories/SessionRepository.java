package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.util.Date;

public interface SessionRepository {
    Session getSessionById(int id) throws SessionNotFoundException;

    Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date endedAt);

    void updateSession(Session s) throws SessionNotFoundException;

    void deleteSession(int id) throws SessionNotFoundException;

    boolean exists(int id);
}
