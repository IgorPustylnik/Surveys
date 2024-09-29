package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.AdminRepository;

import java.util.HashMap;

public class AdminRepositoryMock implements AdminRepository {

    private HashMap<Integer, Admin> admins;

    @Override
    public Admin getAdminByUserId(int userId) {
        return admins.get(userId);
    }

    @Override
    public void addAdmin(Admin a) {
        admins.put(a.getUserId(), a);
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
