package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.AnswerRepository;

public class AnswerRepositoryMock implements AnswerRepository {

    private final DBTableImitation<Answer, Answer> answers = new DBTableImitation<>(1000,
            params -> new Answer((Integer) params[0], (Integer) params[1]),
            Answer::getSelf);

    @Override
    public Answer getAnswerBySelf(Answer a) {
        return answers.get(a);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) {
        answers.add(sessionId, optionId);
    }

    @Override
    public void deleteAnswer(int questionId, int optionId) {
        answers.remove(new Answer(questionId, optionId));
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        return answers.containsKey(new Answer(sessionId, optionId));
    }

}
