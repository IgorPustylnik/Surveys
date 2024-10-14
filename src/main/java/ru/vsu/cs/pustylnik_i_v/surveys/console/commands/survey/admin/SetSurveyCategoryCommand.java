package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;

public class SetSurveyCategoryCommand extends AppCommand {

    public SetSurveyCategoryCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Set category";
    }

    @Override
    public void execute() {
        String categoryName, validation;

        do {
            categoryName = ConsoleUtils.inputString("category name");
            validation = ValidationUtils.isValidName(categoryName);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        ResponseEntity<?> response = appContext.getSurveysService().setSurveyCategory(appContext.currentSurvey.getId(), categoryName);
        ConsoleUtils.clear();
        System.out.println(response.getMessage());
        appContext.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
    }
}
