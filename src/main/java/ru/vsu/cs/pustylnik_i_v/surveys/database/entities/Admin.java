package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

public class Admin {

    private int userId;
    private String email;

    public Admin(int userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
