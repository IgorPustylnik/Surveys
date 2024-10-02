package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.roles.Role;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.service.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.service.SurveysServiceImpl;

import java.util.List;

public class ConsoleAppData {

    private static ConsoleAppData instance;
    private static final SurveysService surveysService = SurveysServiceImpl.getInstance();

    private String token = null;
    private String userName = null;
    private Role role = Role.ANONYMOUS;

    // Surveys list
    private Category category = null;
    private Integer currentPageIndex = 1;

    // Current survey
    private Survey currentSurvey = null;
    private Integer currentQuestionIndex = 1;
    private List<Integer> chosenOptionIndices = null;

    // Session
    private Integer currentSessionId = null;

    public SurveysService getService() {
        return surveysService;
    }

    public void setLocalToken(String token) {
        this.token = token;
    }

    public String getLocalToken() {
        return token;
    }

    public String getLocalUserName() {
        return userName;
    }

    public void setLocalUserName(String userName) {
        this.userName = userName;
    }

    public Role getLocalRole() {
        return role;
    }

    public void setLocalRole(Role role) {
        this.role = role;
    }

    public Integer getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(Integer currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public Survey getCurrentSurvey() {
        return currentSurvey;
    }

    public void setCurrentSurvey(Survey currentSurvey) {
        this.currentSurvey = currentSurvey;
    }

    public Integer getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public List<Integer> getChosenOptionIndices() {
        return chosenOptionIndices;
    }

    public void setChosenOptionIndices(List<Integer> chosenOptionIndices) {
        this.chosenOptionIndices = chosenOptionIndices;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static ConsoleAppData getInstance() {
        if (instance == null) {
            instance = new ConsoleAppData();
        }
        return instance;
    }

    public Integer getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(Integer currentSessionId) {
        this.currentSessionId = currentSessionId;
    }
}
