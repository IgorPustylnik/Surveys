package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Category;

public interface CategoryRepository {
    Category getCategoryById(int id);

    void addCategory(Category c);

    void updateCategory(Category c);

    void deleteCategory(int id);

    boolean exists(int id);
}
