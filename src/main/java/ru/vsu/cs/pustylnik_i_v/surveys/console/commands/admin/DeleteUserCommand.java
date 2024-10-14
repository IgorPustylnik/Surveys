package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class DeleteUserCommand extends AppCommand {

    public DeleteUserCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Delete user";
    }

    @Override
    public void execute() {
        Boolean confirmation = ConsoleUtils.confirm("delete this user");

        if (confirmation == null || !confirmation) {
            appContext.getCommandExecutor().getCommand(CommandType.OPEN_USER).execute();
            return;
        }
        ResponseEntity<?> response = appContext.getUserInfoService().deleteUser(appContext.selectedUser.getName());

        ConsoleUtils.clear();
        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }

        appContext.getCommandExecutor().getCommand(CommandType.LIST_USERS).execute();
    }
}
