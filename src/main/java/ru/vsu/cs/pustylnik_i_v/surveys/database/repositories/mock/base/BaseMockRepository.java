package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base;

import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;

public abstract class BaseMockRepository {

    protected MockDatabaseSource database;

    public BaseMockRepository(MockDatabaseSource database) {
        this.database = database;
    }

}
