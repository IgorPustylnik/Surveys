package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.QuestionRepository;

import java.util.HashMap;

public class QuestionRepositoryMock implements QuestionRepository {

    private HashMap<Integer, Question> questions;

    @Override
    public Question getQuestionById(int id) {
        return questions.get(id);
    }

    @Override
    public void addQuestion(Question q) {
        questions.put(q.getId(), q);
    }

    @Override
    public void updateQuestion(Question q) {
        questions.get(q.getId()).setSurveyId(q.getSurveyId());
        questions.get(q.getId()).setText(q.getText());
        questions.get(q.getId()).setType(q.getType());
    }

    @Override
    public void deleteQuestion(int id) {
        questions.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return questions.containsKey(id);
    }

}
