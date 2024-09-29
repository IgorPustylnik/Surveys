package ru.vsu.cs.pustylnik_i_v.surveys.entities;

public class Answer {

    private int id;
    private int sessionId;
    private int optionId;

    public Answer(int id, int sessionId, int optionId) {
        this.id = id;
        this.sessionId = sessionId;
        this.optionId = optionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
