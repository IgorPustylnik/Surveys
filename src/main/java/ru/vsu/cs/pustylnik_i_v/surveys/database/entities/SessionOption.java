package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

public class SessionOption {
    private int id;
    private int questionId;
    private String description;
    private boolean selected;

    public SessionOption(int id, int questionId, String description, boolean selected) {
        this.id = id;
        this.questionId = questionId;
        this.description = description;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SessionOption{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", description='" + description + '\'' +
                ", selected=" + selected +
                '}';
    }
}
