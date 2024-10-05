package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(int userId) {
      super("Role with userId " + userId + " not found");
    }
}
