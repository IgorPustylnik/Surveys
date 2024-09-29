package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.enums.QuestionType;

public interface QuestionRepository {
    Question getQuestionById(int id);

    void addQuestion(int surveyId, String text, QuestionType type);

    void updateQuestion(Question q);

    void deleteQuestion(int id);

    boolean exists(int id);
}
