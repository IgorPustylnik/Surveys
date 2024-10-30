package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConsoleAppContext {

    private final UserInfoService userInfoService;
    private final SurveysService surveysService;
    private final CommandExecutor commandExecutor;

    // User info
    public User localUser = null;

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

    public String getToken() {
        String filePath = "token.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) {
                return null;
            }
            return lines.get(0);
        } catch (IOException e) {
            return null;
        }
    }

    public void setToken(String token) {
        String filePath = "token.txt";
        try {
            Files.write(Paths.get(filePath), Collections.singletonList(token != null ? token : ""), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
        }
    }

    public void fetchUser() {
        String token = getToken();
        try {
            ResponseEntity<User> response = getUserInfoService().getUser(token);

            if (!response.isSuccess()) {
                localUser = null;
                return;
            }

            localUser = response.getBody();

        } catch (DatabaseAccessException e) {
            System.err.println(e.getMessage());
        }
    }
}
