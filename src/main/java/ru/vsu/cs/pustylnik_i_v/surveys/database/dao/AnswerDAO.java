package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.util.List;

public interface AnswerDAO {
    void putAnswersToQuestion(int sessionId, List<Integer> optionId) throws SessionNotFoundException, OptionNotFoundException, DatabaseAccessException;

    boolean exists(int sessionId, int optionId) throws DatabaseAccessException;
}
