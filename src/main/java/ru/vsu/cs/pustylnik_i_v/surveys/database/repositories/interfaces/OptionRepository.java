package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;

public interface OptionRepository {
    Option getOptionById(int id);

    void addOption(int questionId, String description);

    void updateOption(Option o);

    void deleteOption(int id);

    boolean exists(int id);
}
