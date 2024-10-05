package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoServiceImpl;

import java.util.List;

public class ConsoleAppData {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;
    private CommandExecutor commandExecutor;

    // User info
    public String token = null;
    public String userName = null;
    public RoleType roleType = null;

    // Surveys list
    public Category category = null;
    public Integer currentPageIndex = 1;

    // Current survey
    public Survey currentSurvey = null;
    public Integer currentQuestionIndex = 1;
    public List<Integer> chosenOptionIndices = null;

    // Session
    public Integer currentSessionId = null;

    public ConsoleAppData(UserInfoService userInfoService, SurveysService surveysService) {
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
