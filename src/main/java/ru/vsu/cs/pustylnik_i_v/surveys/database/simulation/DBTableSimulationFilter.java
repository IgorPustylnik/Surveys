package ru.vsu.cs.pustylnik_i_v.surveys.database.simulation;

import java.util.function.Predicate;

public record DBTableSimulationFilter<T>(Predicate<T> condition) {
    public static <T> DBTableSimulationFilter<T> of(Predicate<T> condition) {
        return new DBTableSimulationFilter<>(condition);
    }
}
