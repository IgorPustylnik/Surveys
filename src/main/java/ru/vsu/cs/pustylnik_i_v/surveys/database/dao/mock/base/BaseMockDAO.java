package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base;

import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;

public abstract class BaseMockDAO {

    protected final MockDatabaseSource database;

    public BaseMockDAO(MockDatabaseSource database) {
        this.database = database;
    }

}
