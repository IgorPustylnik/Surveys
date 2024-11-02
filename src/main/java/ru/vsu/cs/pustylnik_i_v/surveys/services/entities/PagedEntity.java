package ru.vsu.cs.pustylnik_i_v.surveys.services.entities;

public record PagedEntity<T>(Integer currentIndex, Integer size, T page) {
}
