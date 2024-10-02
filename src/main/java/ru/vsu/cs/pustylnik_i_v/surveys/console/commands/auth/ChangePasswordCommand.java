package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.ResponseEntity;

public class ChangePasswordCommand extends AppCommand {

    @Override
    public String getName() {
        return "Change password";
    }

    @Override
    public void execute() {
        String oldPassword = ConsoleUtils.inputString("your old password");

        ResponseEntity<?> response = appData.getService().checkIfPasswordIsCorrect(appData.getLocalUserName(), oldPassword);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        String newPassword = ConsoleUtils.inputString("your new password");

        response = appData.getService().updatePassword(appData.getLocalUserName(), newPassword);
        ConsoleUtils.clear();
        System.out.println(response.getMessage());
        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
