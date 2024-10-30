package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.List;

public interface UserRepository {

    User getUser(String name) throws UserNotFoundException, DatabaseAccessException;

    User getUser(int id) throws UserNotFoundException, DatabaseAccessException;

    PagedEntity<List<User>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException;

    void addUser(String name, RoleType roleType, String password) throws DatabaseAccessException;

    void updateUser(User u) throws UserNotFoundException, DatabaseAccessException;

    void deleteUser(String name) throws DatabaseAccessException;

    boolean exists(int userId) throws DatabaseAccessException;

    boolean exists(String name) throws DatabaseAccessException;
}
