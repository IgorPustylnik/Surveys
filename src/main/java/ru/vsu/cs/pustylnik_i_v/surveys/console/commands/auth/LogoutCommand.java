package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;

public class LogoutCommand extends AppCommand {

    public LogoutCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Log out";
    }

    @Override
    public void execute() {
        Boolean confirmation = ConsoleUtils.confirm("log out");

        ConsoleUtils.clear();
        if (confirmation == null) {
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        if (confirmation) {
            appContext.setToken(null);
            appContext.localUser = null;
            appContext.currentCategory = null;
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
