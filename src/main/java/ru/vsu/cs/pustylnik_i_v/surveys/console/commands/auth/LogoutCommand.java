package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

public class LogoutCommand extends AppCommand {

    public LogoutCommand(ConsoleAppData appData) {
        super(appData);
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
            appData.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        if (confirmation) {
            appData.token = null;
            appData.userName = null;
            appData.roleType = null;
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
