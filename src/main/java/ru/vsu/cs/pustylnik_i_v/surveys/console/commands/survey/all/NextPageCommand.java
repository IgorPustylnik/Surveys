package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;

public class NextPageCommand extends AppCommand {
    @Override
    public String getName() {
        return "Next page";
    }

    @Override
    public void execute() {
        appData.setCurrentPageIndex(appData.getCurrentPageIndex() + 1);
        factory.getCommand(CommandType.LIST_SURVEYS).execute();
    }
}
