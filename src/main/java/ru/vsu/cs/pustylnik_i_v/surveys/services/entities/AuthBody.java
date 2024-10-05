package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

public class AuthBody {
    private final RoleType roleType;
    private final String token;

    public AuthBody(RoleType roleType, String token) {
        this.roleType = roleType;
        this.token = token;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return String.format("AuthBody{role=%s, token=%s}", roleType, token);
    }
}
