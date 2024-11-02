package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import java.util.List;

public interface OptionRepository {
    List<Option> getOptions(int questionId) throws DatabaseAccessException;

    void addOption(int questionId, String description) throws DatabaseAccessException;
}
