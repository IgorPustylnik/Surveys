package ru.vsu.cs.pustylnik_i_v.surveys.json;

import java.util.List;

public record QuestionDTO(String description, String type, List<String> options) {
}
