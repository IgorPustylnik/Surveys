package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.AnswerRepository;

import java.util.HashMap;

public class AnswerRepositoryMock implements AnswerRepository {

    private HashMap<Integer, Answer> answers;

    @Override
    public Answer getAnswerById(int id) {
        return answers.get(id);
    }

    @Override
    public void addAnswer(Answer a) {
        answers.put(a.getId(), a);
    }

    @Override
    public void updateAnswer(Answer a) {
        answers.get(a.getId()).setSessionId(a.getSessionId());
        answers.get(a.getId()).setOptionId(a.getOptionId());
    }

    @Override
    public void deleteAnswer(int id) {
        answers.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return answers.containsKey(id);
    }

}
