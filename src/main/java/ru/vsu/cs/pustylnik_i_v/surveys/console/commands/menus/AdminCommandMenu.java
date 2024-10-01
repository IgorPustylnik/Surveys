package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;

import java.util.Arrays;

public class AdminCommandMenu extends CommandMenu {

    public AdminCommandMenu() {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.CREATE_SURVEY,
                        CommandType.ASSIGN_ADMIN,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT)
        );
        setTitle(String.format("Hello, %s (administrator)", appData.getLocalUserName()));
    }

    @Override
    public String getName() {
        return "Main menu";
    }
}
