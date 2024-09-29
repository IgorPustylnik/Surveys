package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;

public interface AdminRepository {
    public Admin getAdminById(int id);

    public void addAdmin(Admin a);

    public void updateAdmin(Admin a);

    public void deleteAdmin(int id);

    public boolean exists(int id);
}
