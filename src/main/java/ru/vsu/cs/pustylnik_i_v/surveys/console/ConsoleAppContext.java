package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public class ConsoleAppContext {

    private final UserService userService;
    private final SurveyService surveyService;
    private final SessionService sessionService;
    private final CommandExecutor commandExecutor;

    // User info
    public Integer localUserId = null;

    // Surveys
    public Integer currentSurveyId = null;
    public Integer currentQuestionIndex = 0;

    // Categories
    public Category selectedCategory = null; // Is being viewed
    public Category currentCategory = null; // Is saved for filtering surveys

    // Users
    public Integer selectedUserId = null; // Is being viewed

    // Pages
    public Integer currentPageIndex = 0;

    // Session
    public Integer currentSessionId = null;

    public ConsoleAppContext(UserService userService, SurveyService surveyService, SessionService sessionService) {
        this.userService = userService;
        this.surveyService = surveyService;
        this.sessionService = sessionService;
        this.commandExecutor = new CommandExecutor(this);
    }

    public UserService getUserService() {
        return userService;
    }

    public SurveyService getSurveyService() {
        return surveyService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public User localUser() {
        try {
            return userService.getUser(localUserId).body();
        } catch (DatabaseAccessException e) {
            getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
        }
        return null;
    }

    public User selectedUser() {
        try {
            return userService.getUser(selectedUserId).body();
        } catch (DatabaseAccessException e) {
            getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
        }
        return null;
    }

    public Survey currentSurvey() {
        try {
            return surveyService.getSurvey(currentSurveyId).body();
        } catch (DatabaseAccessException e) {
            getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
        }
        return null;
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
            ServiceResponse<User> response = getUserService().getUser(token);

            if (!response.success()) {
                localUserId = null;
                return;
            }

            localUserId = response.body().getId();

        } catch (DatabaseAccessException e) {
            System.err.println(e.getMessage());
        }
    }
}
