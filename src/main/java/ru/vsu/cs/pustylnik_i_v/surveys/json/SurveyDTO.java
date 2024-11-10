package ru.vsu.cs.pustylnik_i_v.surveys.json;

import java.util.List;

public record SurveyDTO(String name, String description, String category, List<QuestionDTO> questions) {
}
