package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;

public class ConsoleAppContext {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;
    private final CommandExecutor commandExecutor;

    // User info
    public String token = null;
    public String userName = null;
    public RoleType roleType = null;

    // Surveys
    public Survey currentSurvey = null;
    public Integer currentQuestionIndex = 0;

    // Categories
    public Category selectedCategory = null; // Is being viewed
    public Category currentCategory = null; // Is saved for filtering surveys

    // Users
    public User selectedUser = null; // Is being viewed

    // Pages
    public Integer currentPageIndex = 0;

    // Session
    public Integer currentSessionId = null;

    public ConsoleAppContext(UserInfoService userInfoService, SurveysService surveysService) {
        this.userInfoService = userInfoService;
        this.surveysService = surveysService;
        this.commandExecutor = new CommandExecutor(this);
    }

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }

    public SurveysService getSurveysService() {
        return surveysService;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
