package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;

public interface AnswerRepository {
    Answer getAnswerById(int id);

    void addAnswer(Answer a);

    void updateAnswer(Answer a);

    void deleteAnswer(int id);

    boolean exists(int id);
}
