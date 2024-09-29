package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.UserRepository;

public class UserRepositoryMock implements UserRepository {

    private final DBTableImitation<User, Integer> users = new DBTableImitation<>(1000,
            params -> (new User(0, (String) params[0], (String) params[1])),
            User::getId);

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void addUser(String name, String password) {
        users.add(name, password);
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
