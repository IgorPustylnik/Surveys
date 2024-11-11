package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.SessionQuestion;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.util.List;

public interface SessionQuestionDAO {

    public List<SessionQuestion> getQuestions(int sessionId) throws SessionNotFoundException, DatabaseAccessException;

}
