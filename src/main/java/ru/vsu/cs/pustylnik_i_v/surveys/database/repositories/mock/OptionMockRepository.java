package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;

import java.util.List;

public class OptionMockRepository implements OptionRepository {

    private final DBTableImitation<Option> options = new DBTableImitation<>(
            params -> (new Option(0, (Integer) params[0], (String) params[1])));

    @Override
    public List<Option> getOptions(int questionId) {
        return options.get(Option::getQuestionId,questionId);
    }

    @Override
    public void addOption(int questionId, String description) {
        options.add(questionId, description);
    }
}
