package ru.vsu.cs.pustylnik_i_v.surveys.json;

import java.util.List;

public record EditQuestionDTO(int id, String text, String type, List<EditOptionDTO> options) {
}
