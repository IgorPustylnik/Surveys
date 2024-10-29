package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;

public interface RoleRepository {

    Role getRole(int userId) throws UserNotFoundException, DatabaseAccessException;

    void addRole(int userId, RoleType roleType) throws DatabaseAccessException;

    void updateRole(int userId, RoleType roleType) throws UserNotFoundException, DatabaseAccessException;

    boolean exists(int userId) throws DatabaseAccessException;

}
