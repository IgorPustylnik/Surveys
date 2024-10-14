package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.QuestionRepository;

import java.util.List;

public class QuestionMockRepository implements QuestionRepository {

    private final DBTableImitation<Question> questions = new DBTableImitation<>(
            params -> (new Question(0, (Integer) params[0], (String) params[1], (QuestionType) params[2])));

    @Override
    public List<Question> getQuestions(Integer surveyId) {
        return questions.get(Question::getSurveyId, surveyId);
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type) {
        questions.add(surveyId, text, type);
    }

    @Override
    public boolean exists(int id) {
        return questions.contains(Question::getId, id);
    }

    public boolean exists(String text) {
        return questions.contains(Question::getText, text);
    }

}
