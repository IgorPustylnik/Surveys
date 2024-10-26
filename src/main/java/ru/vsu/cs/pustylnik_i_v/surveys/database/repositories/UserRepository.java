package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.List;

public interface UserRepository {

    User getUser(String name) throws UserNotFoundException;

    List<User> getAllUsers();

    PagedEntity<List<User>> getUsersPagedList(Integer page, Integer perPageAmount);

    void addUser(String name, String password);

    void updateUser(User u) throws UserNotFoundException;

    void deleteUser(String name);

    boolean exists(int userId);

    boolean exists(String name);
}
