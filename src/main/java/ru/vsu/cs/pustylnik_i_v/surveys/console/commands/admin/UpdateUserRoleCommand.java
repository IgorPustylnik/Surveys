package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

public class UpdateUserRoleCommand extends AppCommand {

    public UpdateUserRoleCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Update role";
    }

    @Override
    public void execute() {
        Integer input = ConsoleUtils.inputInt("role type (1 - USER, 2 - ADMIN)");

        if (input == null || (input != 1 && input != 2)) {
            ConsoleUtils.clear();
            appContext.getCommandExecutor().getCommand(CommandType.UNKNOWN).execute();
            this.execute();
            return;
        }

        RoleType roleType = input == 1 ? RoleType.USER : RoleType.ADMIN;

        ServiceResponse<?> response;

        try {
            response = appContext.getUserService().setRole(appContext.selectedUser.getName(), roleType);
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        if (!response.success()) {
            ConsoleUtils.clear();
            System.err.println(response.message());
            appContext.getCommandExecutor().getCommand(CommandType.OPEN_USER).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println(response.message());
        appContext.getCommandExecutor().getCommand(CommandType.OPEN_USER).execute();
    }
}
