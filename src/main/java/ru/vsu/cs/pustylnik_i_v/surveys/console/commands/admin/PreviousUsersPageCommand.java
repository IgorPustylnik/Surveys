package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class PreviousUsersPageCommand extends AppCommand {

    public PreviousUsersPageCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Previous page";
    }

    @Override
    public void execute() {
        appContext.currentPageIndex -= 1;
        appContext.getCommandExecutor().getCommand(CommandType.LIST_USERS).execute();
    }
}
