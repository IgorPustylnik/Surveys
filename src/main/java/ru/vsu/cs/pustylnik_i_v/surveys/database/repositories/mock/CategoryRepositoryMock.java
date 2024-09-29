package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.CategoryRepository;

public class CategoryRepositoryMock implements CategoryRepository {

    private final DBTableImitation<Category, Integer> categories = new DBTableImitation<>(1000,
            params -> new Category(0, (String) params[0]),
            Category::getId);

    @Override
    public Category getCategoryById(int id) {
        return categories.get(id);
    }

    @Override
    public void addCategory(String name) {
        categories.add(name);
    }

    @Override
    public void updateCategory(Category c) {
        categories.get(c.getId()).setName(c.getName());
    }

    @Override
    public void deleteCategory(int id) {
        categories.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return categories.containsKey(id);
    }

}
