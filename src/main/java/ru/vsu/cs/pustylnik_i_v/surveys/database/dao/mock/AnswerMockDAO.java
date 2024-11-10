package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.AnswerDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AnswerMockDAO extends BaseMockDAO implements AnswerDAO {

    public AnswerMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public void addAnswer(int sessionId, int optionId) throws SessionNotFoundException, OptionNotFoundException {
        List<DBTableSimulationFilter<Option>> filterOptions = new ArrayList<>();
        filterOptions.add(DBTableSimulationFilter.of(o -> o.getId() == optionId));
        if (!database.options.contains(filterOptions)) {
            throw new OptionNotFoundException(optionId);
        }

        List<DBTableSimulationFilter<Session>> filterSessions = new ArrayList<>();
        filterSessions.add(DBTableSimulationFilter.of(s -> s.getId() == sessionId));
        if (!database.sessions.contains(filterSessions)) {
            throw new SessionNotFoundException(sessionId);
        }

        database.answers.add(sessionId, optionId);
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        List<DBTableSimulationFilter<Answer>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getSessionId() == sessionId));
        filters.add(DBTableSimulationFilter.of(a -> a.getOptionId() == optionId));

        return database.answers.contains(filters);
    }

}
