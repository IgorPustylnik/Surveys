package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.User;

public interface UserRepository {
    User getUserById(int id);

    void addUser(String name, String password);

    void updateUser(User u);

    void deleteUser(int id);

    boolean exists(int id);
}
