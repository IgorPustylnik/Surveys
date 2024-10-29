package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;

import java.util.List;

public class OptionMockRepository extends BaseMockRepository implements OptionRepository {

    public OptionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<Option> getOptions(int questionId) {
        return database.options.get(Option::getQuestionId,questionId);
    }

    @Override
    public void addOption(int questionId, String description) {
        database.options.add(questionId, description);
    }
}
