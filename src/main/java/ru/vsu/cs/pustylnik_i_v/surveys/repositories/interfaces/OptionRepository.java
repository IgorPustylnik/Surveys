package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Option;

public interface OptionRepository {
    Option getOptionById(int id);

    void addOption(Option o);

    void updateOption(Option o);

    void deleteOption(int id);

    boolean exists(int id);
}
