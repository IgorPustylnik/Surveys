package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.OptionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class OptionMockDAO extends BaseMockDAO implements OptionDAO {

    public OptionMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Option getOption(int id) throws OptionNotFoundException {
        List<DBTableSimulationFilter<Option>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(s -> s.getId() == id));

        List<Option> query = database.options.get(filters);
        if (query.isEmpty()) {
            throw new OptionNotFoundException(id);
        }
        return query.get(0);
    }

    @Override
    public void addOption(int questionId, String description) throws QuestionNotFoundException {
        List<DBTableSimulationFilter<Question>> filtersQuestion = new ArrayList<>();
        filtersQuestion.add(DBTableSimulationFilter.of(q -> q.getId() == questionId));

        List<Question> query = database.questions.get(filtersQuestion);
        if (query.isEmpty()) {
            throw new QuestionNotFoundException(questionId);
        }

        database.options.add(questionId, description);
    }

    @Override
    public void updateOption(Option option) throws OptionNotFoundException {
        List<DBTableSimulationFilter<Option>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(o -> o.getId() == option.getId()));
        List<Option> query = database.options.get(filters);
        if (query.isEmpty()) {
            throw new OptionNotFoundException(option.getId());
        }
        query.get(0).setDescription(option.getDescription());
    }

    @Override
    public void deleteOption(int id) {
        List<DBTableSimulationFilter<Option>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(o -> o.getId() == id));
        database.options.remove(filters);
    }
}
