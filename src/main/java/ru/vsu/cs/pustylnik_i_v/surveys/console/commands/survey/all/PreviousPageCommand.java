package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class PreviousPageCommand extends AppCommand {

    public PreviousPageCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Previous page";
    }

    @Override
    public void execute() {
        appData.currentPageIndex -= 1;
        appData.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
    }
}
