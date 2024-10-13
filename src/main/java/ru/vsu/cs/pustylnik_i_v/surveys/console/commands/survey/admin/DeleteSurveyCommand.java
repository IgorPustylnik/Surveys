package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

public class DeleteSurveyCommand extends AppCommand {

    public DeleteSurveyCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Delete survey";
    }

    @Override
    public void execute() {
        Boolean confirmation = ConsoleUtils.confirm("delete this survey");

        ConsoleUtils.clear();
        if (confirmation != null && confirmation) {
            appContext.getSurveysService().deleteSurvey(appContext.currentSurvey.getId());
            appContext.currentSurvey = null;
            appContext.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
        } else {
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            appContext.getCommandExecutor().getCommand(CommandType.OPEN_SURVEY).execute();
        }
    }
}
