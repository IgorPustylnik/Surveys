package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.User;

public interface UserRepository {
    User getUserById(int id);

    void addUser(User u);

    void updateUser(User u);

    void deleteUser(int id);

    boolean exists(int id);
}
