package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.List;

public class UserMockRepository implements UserRepository {

    private final DBTableImitation<User> users = new DBTableImitation<>(
            params -> (new User(0, (String) params[0], (String) params[1])));

    @Override
    public User getUser(String name) throws UserNotFoundException {
        List<User> query = users.get(User::getName, name);
        if (query.isEmpty()) {
            throw new UserNotFoundException(name);
        }
        return query.get(0);
    }

    @Override
    public User getUser(int id) throws UserNotFoundException {
        List<User> query = users.get(User::getId, id);
        if (query.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return query.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        return users.getAll();
    }

    @Override
    public PagedEntity<List<User>> getUsersPagedList(Integer page, Integer perPageAmount) {
        int fromIndex = perPageAmount * page;
        List<User> sublist = users.getAll().subList(fromIndex, fromIndex + perPageAmount);
        int totalPages = (int) Math.ceil((double) users.size() / perPageAmount);
        if (totalPages < 1) totalPages = 1;
        return new PagedEntity<>(page,totalPages , sublist);
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
