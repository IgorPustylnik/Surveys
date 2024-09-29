package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(int userId) {
        super("User with id " + userId + " not found");
    }
}
