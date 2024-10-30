package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;

public class OpenUserCommand extends CommandMenu {

    public OpenUserCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Open user";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();
        User user = appContext.selectedUser;
        ResponseEntity<RoleType> response;

        try {
            response = appContext.getUserInfoService().getUserRole(user.getName());
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }
        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
        }

        setTitle(String.format("Id: %d\nName: %s\nRole: %s", user.getId(), user.getName(), response.getBody()));

        if (appContext.selectedUser.getId() != appContext.localUser.getId()) {
            commands.add(CommandType.DELETE_USER);
            commands.add(CommandType.UPDATE_USER_ROLE);
        }
        commands.add(CommandType.LIST_USERS);
        super.execute();
    }

}