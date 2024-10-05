package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

public interface UserRepository {

    User getUser(String name) throws UserNotFoundException;

    void addUser(String name, String password);

    void updateUser(User u) throws UserNotFoundException;

    void deleteUser(String name);

    boolean exists(int userId);
}
