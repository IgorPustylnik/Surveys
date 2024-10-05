package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.AnswerRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.AnswerNotFoundException;

import java.util.List;

public class AnswerMockRepository implements AnswerRepository {

    private final DBTableImitation<Answer> answers = new DBTableImitation<>(
            params -> new Answer((Integer) params[0], (Integer) params[1]));

    @Override
    public Answer getAnswerBySelf(Answer a) throws AnswerNotFoundException {
        List<Answer> query = answers.get(Answer::getSelf, a);
        if (query.isEmpty()) {
            throw new AnswerNotFoundException(a);
        }
        return query.get(0);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) {
        answers.add(sessionId, optionId);
    }

    @Override
    public void deleteAnswer(int questionId, int optionId) {
        answers.remove(Answer::getSelf,new Answer(questionId, optionId));
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        return answers.contains(Answer::getSelf,new Answer(sessionId, optionId));
    }

}
