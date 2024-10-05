package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.OptionRepository;

import java.util.List;

public class OptionMockRepository implements OptionRepository {

    private final DBTableImitation<Option> options = new DBTableImitation<>(
            params -> (new Option(0, (Integer) params[0], (String) params[1])));

    @Override
    public Option getOptionById(int id) {
        return options.get(Option::getId,id).get(0);
    }

    @Override
    public List<Option> getOptions(int questionId) {
        return options.get(Option::getQuestionId,questionId);
    }

    @Override
    public void addOption(int questionId, String description) {
        options.add(questionId, description);
    }

    @Override
    public void updateOption(Option o) {
        options.get(Option::getId,o.getId()).get(0).setQuestionId(o.getQuestionId());
        options.get(Option::getId,o.getId()).get(0).setDescription(o.getDescription());
    }

    @Override
    public void deleteOption(int id) {
        options.remove(Option::getId,id);
    }

    @Override
    public boolean exists(int id) {
        return options.contains(Option::getId,id);
    }

}
