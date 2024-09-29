package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.User;

public interface UserRepository {
    public User getUserById(int id);

    public void addUser(User u);

    public void updateUser(User u);

    public void deleteUser(int id);

    public boolean exists(int id);
}
