package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

import java.util.ArrayList;
import java.util.List;

public class MainCommandMenu extends CommandMenu {

    public MainCommandMenu(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Main menu";
    }

    @Override
    public void execute() {
        User localUser = appContext.localUser();
        if (localUser != null && localUser.getRole() == RoleType.BANNED) {
            commands = List.of(
                    CommandType.LOGOUT
            );
            setTitle("You have been banned");
            super.execute();
            return;
        }
        if (localUser == null) {
            commands = List.of(
                    CommandType.LIST_SURVEYS,
                    CommandType.LOGIN,
                    CommandType.REGISTER
            );
            setTitle("Welcome to anonymous-surveys.com");
            super.execute();
            return;
        }
        switch (localUser.getRole()) {
            case USER:
                commands = List.of(CommandType.LIST_SURVEYS,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT
                );
                setTitle(String.format("Hello, %s", localUser.getName()));
                break;
            case ADMIN:
                commands = List.of(CommandType.LIST_SURVEYS,
                        CommandType.CREATE_SURVEY,
                        CommandType.LIST_USERS,
                        CommandType.CHANGE_PASSWORD,
                        CommandType.LOGOUT
                );
                setTitle(String.format("Hello, %s (administrator)", localUser.getName()));
                break;
        }
        super.execute();
    }
}
