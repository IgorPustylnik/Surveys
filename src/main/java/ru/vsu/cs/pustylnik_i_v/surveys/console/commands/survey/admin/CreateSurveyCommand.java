package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

public class CreateSurveyCommand extends AppCommand {

    public CreateSurveyCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Create a survey";
    }

    @Override
    public void execute() {

        String validation;

        String name;
        do {
            name = ConsoleUtils.inputString("a survey name");
            validation = name.length() < 2 ? "Name must be at least 2 characters" : null;
            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        String description = ConsoleUtils.inputString("a description");

        String categoryName;
        do {
            categoryName = ConsoleUtils.inputString("a category name");
            validation = ValidationUtils.isValidName(categoryName);
            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        ServiceResponse<Survey> response;

        try {
            response = appContext.getSurveyService().addSurveyAndGetSelf(name, description, categoryName, appContext.localUser.getId());
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        if (!response.success()) {
            System.err.println(response.message());
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        appContext.currentSurvey = response.body();

        Integer questionsCount;

        do {
            questionsCount = ConsoleUtils.inputInt("questions count");

            validation = (questionsCount == null || questionsCount < 0) ? "Please enter a valid number" : null;
            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        for (int i = 0; i < questionsCount; i++) {
            ConsoleUtils.clear();
            System.out.printf("Question %d of %d\n", i + 1, questionsCount);
            appContext.getCommandExecutor().getCommand(CommandType.ADD_QUESTION).execute();
        }

        ConsoleUtils.clear();
        System.out.println("All questions were added successfully.");
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
