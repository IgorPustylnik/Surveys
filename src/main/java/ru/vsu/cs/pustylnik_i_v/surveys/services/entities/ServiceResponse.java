package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

public record ServiceResponse<T>(boolean success, String message, T body) {
}
