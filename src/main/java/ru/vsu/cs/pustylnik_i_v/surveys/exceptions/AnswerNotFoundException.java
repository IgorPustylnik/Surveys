package ru.vsu.cs.pustylnik_i_v.surveys.exceptions;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Answer;

public class AnswerNotFoundException extends RuntimeException {
    public AnswerNotFoundException(Answer answer) {
        super("Answer with sessionId " + answer.getSessionId() + ", optionId: " + answer.getOptionId() + "not found");
    }
}
