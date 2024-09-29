package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Option;

public interface OptionRepository {
    public Option getOptionById(int id);

    public void addOption(Option o);

    public void updateOption(Option o);

    public void deleteOption(int id);

    public boolean exists(int id);
}
