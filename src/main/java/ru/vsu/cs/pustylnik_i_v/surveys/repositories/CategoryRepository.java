package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Category;

public interface CategoryRepository {
    public Category getCategoryById(int id);

    public void addCategory(Category c);

    public void updateCategory(Category c);

    public void deleteCategory(int id);

    public boolean exists(int id);
}
