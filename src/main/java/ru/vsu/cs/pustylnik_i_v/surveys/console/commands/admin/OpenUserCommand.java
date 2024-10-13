package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
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
        User user = appContext.currentUser;
        ResponseEntity<RoleType> response = appContext.getUserInfoService().getUserRole(user.getName());
        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
        }

        setTitle(String.format("Id: %d\nName: %s\nRole: %s", user.getId(), user.getName(), response.getBody()));

        commands.add(CommandType.DELETE_USER);
        commands.add(CommandType.LIST_USERS);
        super.execute();
    }

}