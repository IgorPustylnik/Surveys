package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class NextSurveysPageCommand extends AppCommand {

    public NextSurveysPageCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Next page";
    }

    @Override
    public void execute() {
        appContext.currentPageIndex += 1;
        appContext.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
    }
}
