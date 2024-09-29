package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class SurveyNotFoundException extends Exception {
    public SurveyNotFoundException(int surveyId) {
        super("Survey with id " + surveyId + " not found");
    }
}
