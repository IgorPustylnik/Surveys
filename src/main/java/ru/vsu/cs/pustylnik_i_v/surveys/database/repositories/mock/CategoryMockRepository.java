package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.CategoryRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;

import java.util.List;

public class CategoryMockRepository implements CategoryRepository {

    private final DBTableImitation<Category> categories = new DBTableImitation<>(
            params -> new Category(0, (String) params[0]));

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {
        List<Category> query = categories.get(Category::getId, id);
        if (query.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        return query.get(0);
    }

    @Override
    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        List<Category> query = categories.get(Category::getName, name);
        if (query.isEmpty()) {
            throw new CategoryNotFoundException(name);
        }
        return query.get(0);
    }

    @Override
    public void addCategory(String name) {
        if (!categories.contains(Category::getName, name)) {
            categories.add(name);
        }
    }

    @Override
    public void updateCategory(Category c) throws CategoryNotFoundException {
        List<Category> query = categories.get(Category::getId,c.getId());
        if (query.isEmpty()) {
            throw new CategoryNotFoundException(c.getId());
        }
        query.get(0).setName(c.getName());
    }

    @Override
    public void deleteCategory(int id) throws CategoryNotFoundException {
        categories.remove(Category::getId,id);
    }

    @Override
    public boolean exists(int id) {
        return categories.contains(Category::getId,id);
    }

    @Override
    public boolean exists(String name) {
        return categories.contains(Category::getName,name);
    }

}
