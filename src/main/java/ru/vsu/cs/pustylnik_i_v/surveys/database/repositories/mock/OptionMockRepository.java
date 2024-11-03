package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class OptionMockRepository extends BaseMockRepository implements OptionRepository {

    public OptionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<Option> getOptions(int questionId) throws QuestionNotFoundException {
        List<DBTableSimulationFilter<Question>> filtersQuestion = new ArrayList<>();
        filtersQuestion.add(DBTableSimulationFilter.of(q -> q.getId() == questionId));

        if (database.questions.get(filtersQuestion).isEmpty()) {
            throw new QuestionNotFoundException(questionId);
        }

        List<DBTableSimulationFilter<Option>> filtersOption = new ArrayList<>();
        filtersOption.add(DBTableSimulationFilter.of(o -> o.getQuestionId() == questionId));

        return database.options.get(filtersOption);
    }

    @Override
    public void addOption(int questionId, String description) throws QuestionNotFoundException {
        List<DBTableSimulationFilter<Question>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(q -> q.getId() == questionId));

        if (database.questions.get(filters).isEmpty()) {
            throw new QuestionNotFoundException(questionId);
        }

        database.options.add(questionId, description);
    }
}
