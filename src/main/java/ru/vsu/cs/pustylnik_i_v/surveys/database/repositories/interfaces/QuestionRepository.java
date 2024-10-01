package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;

import java.util.List;

public interface QuestionRepository {
    Question getQuestionById(int id);

    List<Question> getQuestions(Integer surveyId);

    void addQuestion(int surveyId, String text, QuestionType type);

    void updateQuestion(Question q);

    void deleteQuestion(int id);

    boolean exists(int id);
}
