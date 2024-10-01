package ru.vsu.cs.pustylnik_i_v.surveys.service.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.console.roles.Role;

public class AuthBody {
    private final Role role;
    private final String token;

    public AuthBody(Role role, String token) {
        this.role = role;
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return String.format("AuthBody{role=%s, token=%s}", role, token);
    }
}
