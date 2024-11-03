package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.CategoryRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;

import java.util.List;

public class CategoryMockRepository extends BaseMockRepository implements CategoryRepository {

    public CategoryMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        List<Category> query = database.categories.get(List.of(
                DBTableSimulationFilter.of(Category::getName, name))
        );
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
        if (!database.categories.contains(List.of(
                DBTableSimulationFilter.of(Category::getName, name)))) {
            database.categories.add(name);
        }
    }

    @Override
    public void deleteCategory(int id) {
        database.categories.remove(List.of(
                DBTableSimulationFilter.of(Category::getId, id))
        );
    }

    @Override
    public boolean exists(String name) {
        return database.categories.contains(List.of(
                DBTableSimulationFilter.of(Category::getName, name))
        );
    }

}
