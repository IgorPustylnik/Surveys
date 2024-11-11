package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;

import java.util.List;

public class SessionQuestion {

    private int id;
    private int sessionId;
    private String text;
    private QuestionType type;
    private List<SessionOption> options;

    public SessionQuestion(int id, int sessionId, String text, QuestionType type, List<SessionOption> options) {
        this.id = id;
        this.sessionId = sessionId;
        this.text = text;
        this.type = type;
        this.options = options;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<SessionOption> getOptions() {
        return options;
    }

    public void setOptions(List<SessionOption> options) {
        this.options = options;
    }

    public boolean isAnswered() {
        for (SessionOption option: options) {
            if (option.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "SessionQuestion{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", options=" + options +
                '}';
    }
}