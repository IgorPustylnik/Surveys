package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.AnswerRepository;

public class AnswerRepositoryMock implements AnswerRepository {

    private final DBTableImitation<Answer> answers = new DBTableImitation<>(1000,
            params -> new Answer((Integer) params[0], (Integer) params[1]));

    @Override
    public Answer getAnswerBySelf(Answer a) {
        return answers.get(Answer::getSelf,a).get(0);
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
