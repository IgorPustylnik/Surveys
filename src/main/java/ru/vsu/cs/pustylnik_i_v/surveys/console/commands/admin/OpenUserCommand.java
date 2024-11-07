package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

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
        User localUser = appContext.localUser();
        User selectedUser = appContext.selectedUser();
        ServiceResponse<RoleType> response;

        try {
            response = appContext.getUserService().getUserRole(selectedUser.getName());
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }
        if (!response.success()) {
            System.err.println(response.message());
        }

        setTitle(String.format("Id: %d\nName: %s\nRole: %s", selectedUser.getId(), selectedUser.getName(), response.body()));

        if (selectedUser.getId() != localUser.getId() && !List.of(RoleType.ADMIN, RoleType.BANNED).contains(selectedUser.getRole())) {
            commands.add(CommandType.UPDATE_USER_ROLE);
        }
        if (selectedUser.getId() != localUser.getId() && selectedUser.getRole() != RoleType.ADMIN) {
            commands.add(CommandType.DELETE_USER);
            commands.add(CommandType.TOGGLE_BAN_USER);
        }
        commands.add(CommandType.LIST_USERS);
        super.execute();
    }

}