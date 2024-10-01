package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.console.roles.Role;

public class LogoutCommand extends AppCommand {

    @Override
    public String getName() {
        return "Log out";
    }

    @Override
    public void execute() {
        Boolean confirmation = ConsoleUtils.confirm("log out");

        ConsoleUtils.clear();
        if (confirmation == null) {
            CommandFactory.getInstance().getCommand(CommandType.UNKNOWN).execute();
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        if (confirmation) {
            appData.setLocalToken(null);
            appData.setLocalUserName(null);
            appData.setLocalRole(Role.ANONYMOUS);
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
