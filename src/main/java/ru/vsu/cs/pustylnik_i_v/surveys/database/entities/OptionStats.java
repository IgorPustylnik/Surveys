package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

import java.util.Map;

public record OptionStats(Map<Integer, Integer> optionChosen, int total) {
    public static OptionStats of(Map<Integer, Integer> amount, int total) {
        return new OptionStats(amount, total);
    }
}
