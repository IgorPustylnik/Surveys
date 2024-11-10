package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.CategoryDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CategoryMockDAO extends BaseMockDAO implements CategoryDAO {

    public CategoryMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        List<DBTableSimulationFilter<Category>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getName().equals(name)));

        List<Category> query = database.categories.get(filters);

        if (query.isEmpty()) {
            throw new CategoryNotFoundException(name);
        }

        return query.get(0);
    }

    @Override
    public List<Category> getAllCategories() {
        return database.categories.getAll();
    }

    @Override
    public void addCategory(String name) {
        List<DBTableSimulationFilter<Category>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getName().equals(name)));

        if (!database.categories.contains(filters)) {
            database.categories.add(name);
        }
    }

    @Override
    public void deleteCategory(int id) {
        List<DBTableSimulationFilter<Category>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getId() == id));

        database.categories.remove(filters);
    }

    @Override
    public boolean exists(String name) {
        List<DBTableSimulationFilter<Category>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getName().equals(name)));

        return database.categories.contains(filters);
    }

}
