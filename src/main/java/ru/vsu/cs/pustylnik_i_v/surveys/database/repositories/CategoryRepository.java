package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

import java.util.List;

public interface CategoryRepository {
    Category getCategoryById(int id) throws CategoryNotFoundException, DatabaseAccessException;

    Category getCategoryByName(String name) throws CategoryNotFoundException, DatabaseAccessException;

    List<Category> getAllCategories() throws DatabaseAccessException;

    void addCategory(String name) throws DatabaseAccessException;

    void deleteCategory(int id) throws CategoryNotFoundException, DatabaseAccessException;

    boolean exists(String name) throws DatabaseAccessException;
}
