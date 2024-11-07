package ru.vsu.cs.pustylnik_i_v.surveys.json;

public record TokenDTO(String message, String token) {
    public static TokenDTO of(String message, String token) {
        return new TokenDTO(message, token);
    }
}
