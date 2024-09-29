package ru.vsu.cs.pustylnik_i_v.surveys.mockdb;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.repositories.AdminRepository;

import java.util.HashMap;

public class AdminRepositoryMock implements AdminRepository {

    private HashMap<Integer, Admin> admins;

    @Override
    public Admin getAdminById(int id) {
        return admins.get(id);
    }

    @Override
    public void addAdmin(Admin a) {
        admins.put(a.getId(), a);
    }

    @Override
    public void updateAdmin(Admin a) {
        admins.get(a.getId()).setEmail(a.getEmail());
        admins.get(a.getId()).setName(a.getName());
        admins.get(a.getId()).setPassword(a.getPassword());
    }

    @Override
    public void deleteAdmin(int id) {
        admins.remove(id);
    }

    @Override
    public boolean exists(int id) {
        return admins.containsKey(id);
    }

}
