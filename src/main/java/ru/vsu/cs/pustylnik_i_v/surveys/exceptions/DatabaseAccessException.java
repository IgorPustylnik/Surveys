package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class DatabaseAccessException extends Exception {
    public DatabaseAccessException(String message) {
        super("Failed to access database: " + message);
    }
}
