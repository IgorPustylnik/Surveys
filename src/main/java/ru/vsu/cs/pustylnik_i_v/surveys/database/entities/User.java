package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

public class User{

    private int id;
    private String name;
    private RoleType role;
    private String password;

    public User(int id, String name, RoleType role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
