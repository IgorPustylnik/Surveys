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
            System.err.println(response.getMessage());
        }

        String newPassword = ConsoleUtils.inputString("your new password");

        appData.getService().changePassword(appData.getLocalUserName(), newPassword);
        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
