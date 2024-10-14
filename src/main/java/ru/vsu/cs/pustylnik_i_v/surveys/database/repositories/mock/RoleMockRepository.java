package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.RoleRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.RoleNotFoundException;

import java.util.List;

public class RoleMockRepository implements RoleRepository {

    private final DBTableImitation<Role> roles = new DBTableImitation<>(
            params -> new Role((Integer) params[0], (RoleType) params[1]));

    @Override
    public Role getRole(int userId) throws RoleNotFoundException {
        List<Role> query = roles.get(Role::getUserId, userId);
        if (query.isEmpty()) {
            throw new RoleNotFoundException(userId);
        }
        return query.get(0);
    }

    @Override
    public void addRole(int userId, RoleType role) {
        roles.add(userId, role);
    }

    @Override
    public void updateRole(int userId, RoleType role) throws RoleNotFoundException {
        List<Role> query = roles.get(Role::getUserId, userId);
        if (query.isEmpty()) {
            throw new RoleNotFoundException(userId);
        }
        query.get(0).setRole(role);
    }

    @Override
    public boolean exists(int userId) {
        return roles.contains(Role::getUserId, userId);
    }

}
