package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.UserRepository;

import java.util.List;

public class UserRepositoryMock implements UserRepository {

    private final DBTableImitation<User> users = new DBTableImitation<>(1000,
            params -> (new User(0, (String) params[0], (String) params[1])));

    @Override
    public User getUser(String name) {
        List<User> query = users.get(User::getName, name);
        if (query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    @Override
    public void addUser(String name, String password) {
        users.add(name, password);
    }

    @Override
    public void updateUser(User u) {
        users.get(User::getId, u.getId()).get(0).setName(u.getName());
        users.get(User::getId, u.getId()).get(0).setPassword(u.getPassword());
    }

    @Override
    public void deleteUser(String name) {
        users.remove(User::getName, name);
    }

    @Override
    public boolean exists(int userId) {
        return users.contains(User::getId, userId);
    }

}
