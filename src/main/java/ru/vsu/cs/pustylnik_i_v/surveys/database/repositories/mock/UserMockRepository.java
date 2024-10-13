package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

import java.util.List;

public class UserMockRepository implements UserRepository {

    private final DBTableImitation<User> users = new DBTableImitation<>(
            params -> (new User(0, (String) params[0], (String) params[1], null)));

    @Override
    public User getUser(String name) throws UserNotFoundException {
        List<User> query = users.get(User::getName, name);
        if (query.isEmpty()) {
            throw new UserNotFoundException(name);
        }
        return query.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        return users.getAll();
    }

    @Override
    public void addUser(String name, String password) {
        users.add(name, password);
    }

    @Override
    public void updateUser(User u) throws UserNotFoundException {
        List<User> query = users.get(User::getId, u.getId());
        if (query.isEmpty()) {
            throw new UserNotFoundException(u.getId());
        }
        query.get(0).setName(u.getName());
        query.get(0).setPassword(u.getPassword());
    }

    @Override
    public void deleteUser(String name) {
        users.remove(User::getName, name);
    }

    @Override
    public boolean exists(int userId) {
        return users.contains(User::getId, userId);
    }

    @Override
    public boolean exists(String name) {
        return users.contains(User::getName, name);
    }

}
