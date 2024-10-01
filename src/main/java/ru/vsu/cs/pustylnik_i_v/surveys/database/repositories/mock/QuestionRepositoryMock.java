package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.QuestionRepository;

import java.util.List;

public class QuestionRepositoryMock implements QuestionRepository {

    private final DBTableImitation<Question> questions = new DBTableImitation<>(1000,
            params -> (new Question(0, (Integer) params[0], (String) params[1], (QuestionType) params[2])));

    @Override
    public Question getQuestionById(int id) {
        return questions.get(Question::getId, id).get(0);
    }

    @Override
    public List<Question> getQuestions(Integer surveyId) {
        return questions.get(Question::getSurveyId, surveyId);
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type) {
        questions.add(surveyId, text, type);
    }

    @Override
    public void updateQuestion(Question q) {
        questions.get(Question::getId, q.getId()).get(0).setSurveyId(q.getSurveyId());
        questions.get(Question::getId, q.getId()).get(0).setText(q.getText());
        questions.get(Question::getId, q.getId()).get(0).setType(q.getType());
    }

    @Override
    public void deleteQuestion(int id) {
        questions.remove(Question::getId, id);
    }

    @Override
    public boolean exists(int id) {
        return questions.contains(Question::getId, id);
    }

    public boolean exists(String text) {
        return questions.contains(Question::getText, text);
    }

}
