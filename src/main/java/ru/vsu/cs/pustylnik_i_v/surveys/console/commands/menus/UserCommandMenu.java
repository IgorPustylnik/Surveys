package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;

import java.util.Arrays;

public class UserCommandMenu extends CommandMenu {

    public UserCommandMenu() {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT)
        );
        setTitle(String.format("Hello, %s", appData.getLocalUserName()));
    }

    @Override
    public String getName() {
        return "Main menu";
    }

}
