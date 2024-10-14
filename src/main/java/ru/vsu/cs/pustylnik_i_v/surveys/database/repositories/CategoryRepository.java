package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;

public interface CategoryRepository {
    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByName(String name) throws CategoryNotFoundException;

    void addCategory(String name);

    boolean exists(String name);
}
