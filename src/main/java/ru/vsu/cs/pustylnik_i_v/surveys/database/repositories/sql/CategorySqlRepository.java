package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.CategoryRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.sql.base.BaseSqlRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.sql.DatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.CategoryNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategorySqlRepository extends BaseSqlRepository implements CategoryRepository {
    public CategorySqlRepository(DatabaseSource dataSource) {
        super(dataSource);
    }

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {
        String query = "SELECT * FROM categories WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(resultSet.getInt("id"),
                            resultSet.getString("name"));
                }
            } catch (SQLException e) {
                throw new CategoryNotFoundException(id);
            }

        } catch (SQLException e) {
            throw new CategoryNotFoundException(id);
        }
        throw new CategoryNotFoundException(id);
    }

    @Override
    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        String query = "SELECT * FROM categories WHERE name = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(resultSet.getInt("id"),
                            resultSet.getString("name"));
                }
            }

        } catch (SQLException e) {
            throw new CategoryNotFoundException(name);
        }
        throw new CategoryNotFoundException(name);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM categories";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    categories.add(new Category(resultSet.getInt("id"),
                            resultSet.getString("name")));
                }
            }
        } catch (SQLException ignored) {}
        return categories;
    }

    @Override
    public void addCategory(String name) {
        String query = "INSERT INTO categories (name) VALUES (?)";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);

            statement.executeQuery();
        } catch (SQLException ignored) {}
    }

    @Override
    public void deleteCategory(int id) throws CategoryNotFoundException {
        String query = "DELETE FROM categories WHERE id = ?";

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);

            statement.execute();
        } catch (SQLException e) {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public boolean exists(String name) {
        try {
            getCategoryByName(name);
        } catch (CategoryNotFoundException e) {
            return false;
        }
        return true;
    }
}
