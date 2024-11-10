package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

public interface AnswerDAO {
    void addAnswer(int sessionId, int optionId) throws SessionNotFoundException, OptionNotFoundException, DatabaseAccessException;

    boolean exists(int sessionId, int optionId) throws DatabaseAccessException;
}
