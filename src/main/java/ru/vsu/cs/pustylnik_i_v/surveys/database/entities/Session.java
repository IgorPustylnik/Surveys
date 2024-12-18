package ru.vsu.cs.pustylnik_i_v.surveys.database.entities;

import java.util.Date;

public class Session {

    private int id;
    private int surveyId;
    private String surveyName;
    private Integer userId;
    private Date startedAt;
    private Date finishedAt;

    public Session(int id, int surveyId, String surveyName, Integer userId, Date startedAt, Date finishedAt) {
        this.id = id;
        this.surveyId = surveyId;
        this.surveyName = surveyName;
        this.userId = userId;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
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

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }
}
