package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import java.util.List;

public interface QuestionRepository {
    List<Question> getQuestions(Integer surveyId) throws DatabaseAccessException;

    void addQuestion(int surveyId, String text, QuestionType type) throws DatabaseAccessException;
}
