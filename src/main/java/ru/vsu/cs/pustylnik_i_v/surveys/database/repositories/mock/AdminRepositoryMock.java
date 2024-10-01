package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.AdminRepository;

import java.util.List;

public class AdminRepositoryMock implements AdminRepository {

    private final DBTableImitation<Admin> admins = new DBTableImitation<>(
            1000,
            params -> new Admin((Integer) params[0], (String) params[1]));

    @Override
    public Admin getAdminByUserId(int userId) {
        List<Admin> query = admins.get(Admin::getUserId, userId);
        if (query.isEmpty()) {
            return null;
        }
        return query.get(0);
    }

    @Override
    public void addAdmin(int userId, String password) {
        admins.add(userId, password);
    }

    @Override
    public void updateAdmin(Admin a) {
        admins.get(Admin::getUserId, a.getUserId()).get(0).setEmail(a.getEmail());
    }

    @Override
    public void deleteAdmin(int userId) {
        admins.remove(Admin::getUserId, userId);
    }

    @Override
    public boolean exists(int userId) {
        return admins.contains(Admin::getUserId, userId);
    }

}
