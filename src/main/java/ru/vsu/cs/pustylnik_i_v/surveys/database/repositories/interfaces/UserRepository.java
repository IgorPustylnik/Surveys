package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;

public interface UserRepository {

    User getUser(String name);

    void addUser(String name, String password);

    void updateUser(User u);

    void deleteUser(String name);

    boolean exists(int userId);
}
