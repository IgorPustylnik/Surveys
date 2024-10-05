package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class AssignAdminCommand extends AppCommand {

    public AssignAdminCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Assign an administrator";
    }

    @Override
    public void execute() {
        if (appData.roleType != RoleType.ADMIN) {
            ConsoleUtils.clear();
            System.out.println("No permission");
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        String input = ConsoleUtils.inputString("user name");

        ResponseEntity<?> response = appData.getUserInfoService().setRole(input, RoleType.ADMIN);

        ConsoleUtils.clear();
        if (!response.isSuccess()) {
            System.err.println(response.getMessage());
        } else {
            System.out.println(response.getMessage());
        }

        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
