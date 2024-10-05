package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

public interface AnswerRepository {
    Answer getAnswerBySelf(Answer a);

    void addAnswer(int sessionId, int optionId) throws SessionNotFoundException, OptionNotFoundException;

    void deleteAnswer(int sessionId, int optionId);

    boolean exists(int sessionId, int optionId);
}
