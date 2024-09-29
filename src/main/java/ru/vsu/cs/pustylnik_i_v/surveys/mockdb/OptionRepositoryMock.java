package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.OptionRepository;

import java.util.HashMap;

public class OptionRepositoryMock implements OptionRepository {

    private HashMap<Integer, Option> options;

    @Override
    public Option getOptionById(int id) {
        return options.get(id);
    }

    @Override
    public void addOption(Option o) {
        options.put(o.getId(), o);
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
