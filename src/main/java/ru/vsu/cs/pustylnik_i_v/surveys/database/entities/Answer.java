package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

public class Answer {

    private int sessionId;
    private int optionId;

    public Answer(int sessionId, int optionId) {
        this.sessionId = sessionId;
        this.optionId = optionId;
    }


    public Answer getSelf() {
        return this;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}
