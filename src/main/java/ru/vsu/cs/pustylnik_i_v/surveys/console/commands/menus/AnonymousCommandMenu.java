package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

import java.util.Arrays;

public class AnonymousCommandMenu extends CommandMenu {

    public AnonymousCommandMenu(ConsoleAppContext appContext) {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.LOGIN,
                        CommandType.REGISTER),
                appContext
        );
        setTitle("Welcome to anonymous-surveys.com");
    }

    @Override
    public String getName() {
        return "Main menu";
    }
}
