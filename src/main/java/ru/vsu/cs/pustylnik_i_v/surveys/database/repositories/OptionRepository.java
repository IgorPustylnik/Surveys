package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

import java.util.List;

public interface OptionRepository {
    List<Option> getOptions(int questionId) throws QuestionNotFoundException, DatabaseAccessException;

    void addOption(int questionId, String description) throws QuestionNotFoundException, DatabaseAccessException;
}
