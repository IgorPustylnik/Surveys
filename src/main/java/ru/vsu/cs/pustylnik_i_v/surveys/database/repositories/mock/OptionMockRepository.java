package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

import java.util.List;

public class OptionMockRepository extends BaseMockRepository implements OptionRepository {

    public OptionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<Option> getOptions(int questionId) throws QuestionNotFoundException {
        if (database.questions.get(List.of(
                DBTableSimulationFilter.of(Question::getId, questionId)))
                .isEmpty()) {
            throw new QuestionNotFoundException(questionId);
        }
        return database.options.get(List.of(
                DBTableSimulationFilter.of(Option::getQuestionId, questionId))
        );
    }

    @Override
    public void addOption(int questionId, String description) throws QuestionNotFoundException {
        if (database.questions.get(List.of(
                DBTableSimulationFilter.of(Question::getId, questionId)))
                .isEmpty()) {
            throw new QuestionNotFoundException(questionId);
        }
        database.options.add(questionId, description);
    }
}
