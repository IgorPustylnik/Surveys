package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;

import java.util.List;

public class UserMockRepository extends BaseMockRepository implements UserRepository {
    public UserMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public User getUser(String name) throws UserNotFoundException {
        List<User> query = database.users.get(User::getName, name);
        if (query.isEmpty()) {
            throw new UserNotFoundException(name);
        }
        return query.get(0);
    }

    @Override
    public User getUser(int id) throws UserNotFoundException {
        List<User> query = database.users.get(User::getId, id);
        if (query.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return query.get(0);
    }

    @Override
    public PagedEntity<List<User>> getUsersPagedList(Integer page, Integer perPageAmount) {
        int fromIndex = perPageAmount * page;
        List<User> allUsers = database.users.getAll();

        int toIndex = Math.min(fromIndex + perPageAmount, allUsers.size());

        List<User> sublist = allUsers.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) allUsers.size() / perPageAmount);
        if (totalPages < 1) totalPages = 1;

        return new PagedEntity<>(page, totalPages, sublist);
    }

    @Override
    public void addUser(String name, RoleType roleType, String password) {
        database.users.add(name, roleType, password);
    }

    @Override
    public void updateUser(User u) throws UserNotFoundException {
        List<User> query = database.users.get(User::getId, u.getId());
        if (query.isEmpty()) {
            throw new UserNotFoundException(u.getId());
        }
        query.get(0).setName(u.getName());
        query.get(0).setRole(u.getRole());
        query.get(0).setPassword(u.getPassword());
    }

    @Override
    public void deleteUser(String name) {
        database.users.remove(User::getName, name);
    }

    @Override
    public boolean exists(int userId) {
        return database.users.contains(User::getId, userId);
    }

    @Override
    public boolean exists(String name) {
        return database.users.contains(User::getName, name);
    }

}
