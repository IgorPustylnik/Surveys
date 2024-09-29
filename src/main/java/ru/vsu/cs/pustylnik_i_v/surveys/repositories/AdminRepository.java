package ru.vsu.cs.pustylnik_i_v.surveys.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

public interface AdminRepository {
    public Admin getAdminByUserId(int userId);

    public void addAdmin(Admin a) throws UserNotFoundException;

    public void updateAdmin(Admin a);

    public void deleteAdmin(int userId);

    public boolean exists(int userId);
}
