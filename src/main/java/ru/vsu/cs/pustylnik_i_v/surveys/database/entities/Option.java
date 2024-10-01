package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

public class Option {

    private int id;
    private int questionId;
    private String description;

    public Option(int id, int questionId, String description) {
        this.id = id;
        this.questionId = questionId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
