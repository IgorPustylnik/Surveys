package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

public class QuestionNotFoundException extends Exception {
    public QuestionNotFoundException(int questionId) {
        super("Question with id " + questionId + " not found");
    }
}
