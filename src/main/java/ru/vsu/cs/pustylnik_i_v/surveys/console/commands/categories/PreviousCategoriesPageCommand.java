package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class PreviousCategoriesPageCommand extends AppCommand {

    public PreviousCategoriesPageCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Previous page";
    }

    @Override
    public void execute() {
        appContext.currentPageIndex -= 1;
        appContext.getCommandExecutor().getCommand(CommandType.LIST_CATEGORIES).execute();
    }
}
