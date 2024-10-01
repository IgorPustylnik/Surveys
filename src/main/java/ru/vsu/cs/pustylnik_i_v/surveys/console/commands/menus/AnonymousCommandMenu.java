package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;

import java.util.Arrays;

public class AnonymousCommandMenu extends CommandMenu {

    public AnonymousCommandMenu() {
        super(Arrays.asList(
                        CommandType.LIST_SURVEYS,
                        CommandType.LOGIN,
                        CommandType.REGISTER)
        );
        setTitle("Welcome to anonymous-surveys.com");
    }

    @Override
    public String getName() {
        return "Main menu";
    }
}
