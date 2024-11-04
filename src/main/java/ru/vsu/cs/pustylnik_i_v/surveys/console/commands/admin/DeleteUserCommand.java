package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

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
        ServiceResponse<?> response;
        try {
            response = appContext.getUserService().deleteUser(appContext.selectedUser.getName());
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        ConsoleUtils.clear();
        if (!response.success()) {
            System.err.println(response.message());
        } else {
            System.out.println(response.message());
        }

        appContext.getCommandExecutor().getCommand(CommandType.LIST_USERS).execute();
    }
}
