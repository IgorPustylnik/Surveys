package ru.vsu.cs.pustylnik_i_v.surveys.json;

import java.util.List;

public record EditSurveyDTO(int id, String name, String description, String category, List<EditQuestionDTO> questions) {
}
