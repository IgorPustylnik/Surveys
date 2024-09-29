package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;

public interface AnswerRepository {
    Answer getAnswerBySelf(Answer a);

    void addAnswer(int sessionId, int optionId);

    void deleteAnswer(int sessionId, int optionId);

    boolean exists(int sessionId, int optionId);
}
