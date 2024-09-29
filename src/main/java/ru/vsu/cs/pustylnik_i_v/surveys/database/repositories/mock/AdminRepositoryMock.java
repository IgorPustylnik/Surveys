package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces.AdminRepository;

public class AdminRepositoryMock implements AdminRepository {

    private final DBTableImitation<Admin, Integer> admins = new DBTableImitation<>(
            1000,
            params -> new Admin((Integer) params[0], (String) params[1]),
            Admin::getUserId);

    @Override
    public Admin getAdminByUserId(int userId) {
        return admins.get(userId);
    }

    @Override
    public void addAdmin(int userId, String password) {
        admins.add(userId, password);
    }

    @Override
    public void updateAdmin(Admin a) {
        admins.get(a.getUserId()).setEmail(a.getEmail());
    }

    @Override
    public void deleteAdmin(int userId) {
        admins.remove(userId);
    }

    @Override
    public boolean exists(int userId) {
        return admins.containsKey(userId);
    }

}
