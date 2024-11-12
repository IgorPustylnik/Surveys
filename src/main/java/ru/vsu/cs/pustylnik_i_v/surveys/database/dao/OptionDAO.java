package ru.vsu.cs.pustylnik_i_v.surveys.database.dao;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.QuestionNotFoundException;

public interface OptionDAO {

    Option getOption(int id) throws OptionNotFoundException, DatabaseAccessException;

    void addOption(int questionId, String description) throws QuestionNotFoundException, DatabaseAccessException;

    void updateOption(Option option) throws DatabaseAccessException, OptionNotFoundException;

    void deleteOption(int id) throws DatabaseAccessException;

}
