package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

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
        String name = ConsoleUtils.inputString("a survey name");
        String description = ConsoleUtils.inputString("a description");

        String categoryName, validation;
        do {
            categoryName = ConsoleUtils.inputString("a category name");
            validation = ValidationUtils.isValidName(categoryName);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        ResponseEntity<Survey> response = appContext.getSurveysService().addSurveyAndGetSelf(name, description, categoryName);

        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        appContext.currentSurvey = response.getBody();

        Integer questionsCount = ConsoleUtils.inputInt("questions count");

        if (questionsCount == null || questionsCount < 0) {
            ConsoleUtils.clear();
            System.err.println("Please enter a valid number");
            this.execute();
            return;
        }

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
