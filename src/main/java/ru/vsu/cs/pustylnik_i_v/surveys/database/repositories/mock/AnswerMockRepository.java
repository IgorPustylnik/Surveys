package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.AnswerRepository;

public class AnswerMockRepository implements AnswerRepository {

    private final DBTableImitation<Answer> answers = new DBTableImitation<>(
            params -> new Answer((Integer) params[0], (Integer) params[1]));
    @Override
    public void addAnswer(int sessionId, int optionId) {
        answers.add(sessionId, optionId);
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        return answers.contains(Answer::getSelf,new Answer(sessionId, optionId));
    }

}
