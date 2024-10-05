package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class ChangePasswordCommand extends AppCommand {

    public ChangePasswordCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Change password";
    }

    @Override
    public void execute() {
        String oldPassword = ConsoleUtils.inputString("your old password");

        ResponseEntity<?> response = appData.getUserInfoService().checkIfPasswordIsCorrect(appData.userName, oldPassword);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        String newPassword = ConsoleUtils.inputString("your new password");

        response = appData.getUserInfoService().updatePassword(appData.userName, newPassword);
        ConsoleUtils.clear();
        System.out.println(response.getMessage());
        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
