package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Question;

public interface QuestionRepository {
    public Question getQuestionById(int id);

    public void addQuestion(Question q);

    public void updateQuestion(Question q);

    public void deleteQuestion(int id);

    public boolean exists(int id);
}
