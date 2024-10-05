package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import java.util.Arrays;

public class UserCommandMenu extends CommandMenu {

    public UserCommandMenu(ConsoleAppData appData) {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT),
                appData
        );
    }

    @Override
    public String getName() {
        return "Main menu";
    }

    @Override
    public void execute() {
        setTitle(String.format("Hello, %s", appData.userName));
        super.execute();
    }

}
