package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.AnswerRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;

import java.util.List;

public class AnswerMockRepository extends BaseMockRepository implements AnswerRepository {

    public AnswerMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) {
        database.answers.add(sessionId, optionId);
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        return database.answers.contains(List.of(
                DBTableSimulationFilter.of(Answer::getSessionId, sessionId),
                DBTableSimulationFilter.of(Answer::getOptionId, optionId))
        );
    }

}
