package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.CategoryRepository;

import java.util.List;

public class CategoryRepositoryMock implements CategoryRepository {

    private final DBTableImitation<Category> categories = new DBTableImitation<>(1000,
            params -> new Category(0, (String) params[0]));

    @Override
    public Category getCategoryById(int id) {
        List<Category> query = categories.get(Category::getId, id);
        if (query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    @Override
    public Category getCategoryByName(String name) {
        List<Category> query = categories.get(Category::getName, name);
        if (query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    @Override
    public void addCategory(String name) {
        categories.add(name);
    }

    @Override
    public void updateCategory(Category c) {
        categories.get(Category::getId,c.getId()).get(0).setName(c.getName());
    }

    @Override
    public void deleteCategory(int id) {
        categories.remove(Category::getId,id);
    }

    @Override
    public boolean exists(int id) {
        return categories.contains(Category::getId,id);
    }

}
