package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

import java.io.Serializable;

public class TokenInfo implements Serializable {
    private final int id;

    public TokenInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
