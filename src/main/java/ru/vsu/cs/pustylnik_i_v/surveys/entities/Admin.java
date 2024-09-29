package ru.vsu.cs.pustylnik_i_v.surveys.entities;

public class Admin extends User {

    private String email;

    public Admin(int id, String name, String email, String password) {
        super(id, name, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
