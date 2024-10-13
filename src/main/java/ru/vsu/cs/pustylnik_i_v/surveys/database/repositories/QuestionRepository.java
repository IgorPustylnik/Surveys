package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

import java.util.List;

public interface QuestionRepository {
    Question getQuestionById(int id) throws QuestionNotFoundException;

    List<Question> getQuestions(Integer surveyId);

    void addQuestion(int surveyId, String text, QuestionType type);

    void updateQuestion(Question q) throws QuestionNotFoundException;

    void deleteQuestion(int id) throws QuestionNotFoundException;

    boolean exists(int id);
}