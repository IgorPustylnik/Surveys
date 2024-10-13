package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

import java.util.Arrays;

public class AdminCommandMenu extends CommandMenu {

    public AdminCommandMenu(ConsoleAppContext appContext) {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.CREATE_SURVEY,
                        CommandType.LIST_USERS,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT),
                appContext
        );
    }

    @Override
    public String getName() {
        return "Main menu";
    }

    @Override
    public void execute() {
        setTitle(String.format("Hello, %s (administrator)", appContext.userName));
        super.execute();
    }
}
