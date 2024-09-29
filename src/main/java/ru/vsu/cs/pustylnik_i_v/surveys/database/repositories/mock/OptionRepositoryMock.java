package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.OptionRepository;

public class OptionRepositoryMock implements OptionRepository {

    private final DBTableImitation<Option, Integer> options = new DBTableImitation<>(1000,
            params -> (new Option(0, (Integer) params[0], (String) params[1])),
            Option::getId);

    @Override
    public Option getOptionById(int id) {
        return options.get(id);
    }

    @Override
    public void addOption(int questionId, String description) {
        options.add(questionId, description);
    }

    @Override
    public void updateOption(Option o) {
        options.get(o.getId()).setQuestionId(o.getQuestionId());
        options.get(o.getId()).setDescription(o.getDescription());
    }

    @Override
    public void deleteOption(int id) {
        options.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return options.containsKey(id);
    }

}
