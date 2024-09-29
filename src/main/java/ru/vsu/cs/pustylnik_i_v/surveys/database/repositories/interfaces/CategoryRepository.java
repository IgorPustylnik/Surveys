package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Category;

public interface CategoryRepository {
    Category getCategoryById(int id);

    void addCategory(String name);

    void updateCategory(Category c);

    void deleteCategory(int id);

    boolean exists(int id);
}
