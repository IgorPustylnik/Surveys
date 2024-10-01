package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;

public class Question {

    private int id;
    private int surveyId;
    private String text;
    private QuestionType type;

    public Question(int id, int surveyId, String text, QuestionType type) {
        this.id = id;
        this.surveyId = surveyId;
        this.text = text;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
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
}
