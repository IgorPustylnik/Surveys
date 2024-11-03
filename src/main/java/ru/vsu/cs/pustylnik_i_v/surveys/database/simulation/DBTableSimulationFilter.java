package ru.vsu.cs.pustylnik_i_v.surveys.database.simulation;

import java.util.function.Function;

public record DBTableSimulationFilter<T>(Function<T, ?> keyFunction, Object key) {
    public static <T> DBTableSimulationFilter<T> of(Function<T, ?> keyFunction, Object key) {
        return new DBTableSimulationFilter<>(keyFunction, key);
    }
}
