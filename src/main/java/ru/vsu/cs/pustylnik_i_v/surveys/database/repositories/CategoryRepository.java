package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;

import java.util.List;

public interface CategoryRepository {
    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByName(String name) throws CategoryNotFoundException;

    List<Category> getAllCategories();

    void addCategory(String name);

    void deleteCategory(int id) throws CategoryNotFoundException;

    boolean exists(String name);
}
