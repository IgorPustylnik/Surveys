package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.CategoryRepository;

import java.util.HashMap;

public class CategoryRepositoryMock implements CategoryRepository {

    private HashMap<Integer, Category> categories;

    @Override
    public Category getCategoryById(int id) {
        return categories.get(id);
    }

    @Override
    public void addCategory(Category c) {
        categories.put(c.getId(), c);
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
