package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;


import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

public class Role {

    private int userId;
    private RoleType roleType;

    public Role(int userId, RoleType roleType) {
        this.userId = userId;
        this.roleType = roleType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRole(RoleType roleType) {
        this.roleType = roleType;
    }
}
