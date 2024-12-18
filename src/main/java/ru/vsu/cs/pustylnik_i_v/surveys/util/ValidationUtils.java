package ru.vsu.cs.pustylnik_i_v.surveys.util;

public class ValidationUtils {

    public static String isValidUserName(String name) {
        if (name == null || name.length() < 2) {
            return "The name must be at least 2 characters long";
        }
        if (name.length() > 30) {
            return "The name is too long";
        }
        if (!name.matches("^[a-zA-Z0-9]+$")) {
            return "The name can only contain latin letters and digits";
        }
        return null;
    }

    public static String isValidName(String name) {
        if (name == null || name.length() < 2) {
            return "The name must be at least 2 characters long";
        }
        if (name.length() > 255) {
            return "The name is too long";
        }
        return null;
    }

    public static String isValidDescription(String description) {
        if (description.length() > 1000) {
            return "The description is too long";
        }
        return null;
    }

    public static String isValidPassword(String password) {
        if (password.length() < 8) {
            return "The password must be at least 8 characters long";
        }
        if (password.length() > 50) {
            return "The password is too long";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "The password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[0-9].*")) {
            return "The password must contain at least one digit";
        }
        if (!password.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$")) {
            return "The password contains invalid characters";
        }
        return null;
    }

}
