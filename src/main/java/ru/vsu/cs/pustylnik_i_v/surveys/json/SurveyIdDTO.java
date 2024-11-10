package ru.vsu.cs.pustylnik_i_v.surveys.json;

public record SurveyIdDTO(int surveyId) {
    public static SurveyIdDTO of(int surveyId) {
        return new SurveyIdDTO(surveyId);
    }
}
