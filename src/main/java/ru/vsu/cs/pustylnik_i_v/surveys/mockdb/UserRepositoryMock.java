package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.UserRepository;

import java.util.HashMap;

public class UserRepositoryMock implements UserRepository {

    private HashMap<Integer, User> users;

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void addUser(User u) {
        users.put(u.getId(), u);
    }

    @Override
    public void updateUser(User u) {
        users.get(u.getId()).setName(u.getName());
        users.get(u.getId()).setPassword(u.getPassword());
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return users.containsKey(id);
    }

}
