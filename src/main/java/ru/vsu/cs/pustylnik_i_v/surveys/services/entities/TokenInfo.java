package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

import java.io.Serializable;

public class TokenInfo implements Serializable {
    private final int id;
    private final RoleType roleType;

    public TokenInfo(int id, RoleType roleType) {
        this.id = id;
        this.roleType = roleType;
    }

    public int getId() {
        return id;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
