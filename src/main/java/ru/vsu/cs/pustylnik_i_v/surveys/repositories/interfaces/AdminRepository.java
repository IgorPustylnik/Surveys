package ru.vsu.cs.pustylnik_i_v.surveys.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;

public interface AdminRepository {
    Admin getAdminByUserId(int userId);

    void addAdmin(Admin a);

    void updateAdmin(Admin a);

    void deleteAdmin(int userId);

    boolean exists(int userId);
}
