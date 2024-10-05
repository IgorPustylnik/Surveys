package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(int categoryId) {
        super("Category with id " + categoryId + " not found");
    }

    public CategoryNotFoundException(String categoryName) {
        super("Category with id " + categoryName + " not found");
    }
}
