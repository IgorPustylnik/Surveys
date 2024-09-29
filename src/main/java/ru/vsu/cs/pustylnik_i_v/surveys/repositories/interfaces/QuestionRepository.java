package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Question;

public interface QuestionRepository {
    Question getQuestionById(int id);

    void addQuestion(Question q);

    void updateQuestion(Question q);

    void deleteQuestion(int id);

    boolean exists(int id);
}
