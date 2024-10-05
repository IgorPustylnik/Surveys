package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class CreateSurveyCommand extends AppCommand {

    public CreateSurveyCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Create a survey";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("a survey name");
        String description = ConsoleUtils.inputString("a description");
        String categoryName = ConsoleUtils.inputString("a category");

        ResponseEntity<Integer> response = appData.getSurveysService().addSurveyAndGetId(name, description, categoryName);

        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        System.out.println(response.getMessage());
        ConsoleUtils.clear();

        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
