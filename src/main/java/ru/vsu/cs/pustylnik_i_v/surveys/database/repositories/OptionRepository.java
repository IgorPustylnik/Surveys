package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;

import java.util.List;

public interface OptionRepository {
    Option getOptionById(int id);

    List<Option> getOptions(int questionId);

    void addOption(int questionId, String description);

    void updateOption(Option o);

    void deleteOption(int id);

    boolean exists(int id);
}
