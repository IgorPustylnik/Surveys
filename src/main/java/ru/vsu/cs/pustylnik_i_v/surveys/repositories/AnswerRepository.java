package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;

public interface AnswerRepository {
    public Answer getAnswerById(int id);

    public void addAnswer(Answer a);

    public void updateAnswer(Answer a);

    public void deleteAnswer(int id);

    public boolean exists(int id);
}
