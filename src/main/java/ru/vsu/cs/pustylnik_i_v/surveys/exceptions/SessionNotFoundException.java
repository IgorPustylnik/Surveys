package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class SessionNotFoundException extends Exception {
    public SessionNotFoundException(int sessionId) {
        super("Session with id " + sessionId + " not found");
    }
}
