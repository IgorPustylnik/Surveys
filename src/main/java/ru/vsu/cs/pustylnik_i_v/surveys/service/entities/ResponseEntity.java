package ru.vsu.cs.pustylnik_i_v.surveys.service.entities;

public class ResponseEntity<T> {
    private final boolean success;
    private final String message;
    private final T body;

    public ResponseEntity(boolean success, String message, T body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("ResponseEntity{success=%s, message=%s, body=%s}\n", success, message, body);
    }
}
