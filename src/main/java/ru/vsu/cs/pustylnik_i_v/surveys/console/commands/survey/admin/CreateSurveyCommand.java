package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.ResponseEntity;

public class CreateSurveyCommand extends AppCommand {

    @Override
    public String getName() {
        return "Create a survey";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("a survey name");
        String description = ConsoleUtils.inputString("a description");
        String categoryName = ConsoleUtils.inputString("a category");

        ResponseEntity<Integer> response = appData.getService().addSurveyAndGetId(name, description, categoryName);

        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        System.out.println(response.getMessage());
        ConsoleUtils.clear();

        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
