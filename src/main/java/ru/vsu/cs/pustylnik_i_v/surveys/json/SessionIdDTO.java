package ru.vsu.cs.pustylnik_i_v.surveys.json;

public record SessionIdDTO(Integer sessionId) {
    public static SessionIdDTO of(Integer sessionId) {
        return new SessionIdDTO(sessionId);
    }
}
