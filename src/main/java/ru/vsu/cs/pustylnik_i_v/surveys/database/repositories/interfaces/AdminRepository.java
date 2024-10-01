package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.interfaces;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Admin;

public interface AdminRepository {
    Admin getAdminByUserId(int userId);

    void addAdmin(int userId, String password);

    void updateAdmin(Admin a);

    void deleteAdmin(int userId);

    boolean exists(int userId);
}
