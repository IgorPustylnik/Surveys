package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class ChangePasswordCommand extends AppCommand {

    public ChangePasswordCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Change password";
    }

    @Override
    public void execute() {
        String oldPassword = ConsoleUtils.inputString("your old password");

        ResponseEntity<?> response = appContext.getUserInfoService().checkIfPasswordIsCorrect(appContext.userName, oldPassword);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        String newPassword, validation;
        do {
            newPassword = ConsoleUtils.inputString("your new password");
            validation = ValidationUtils.isValidPassword(newPassword);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        response = appContext.getUserInfoService().updatePassword(appContext.userName, newPassword);
        ConsoleUtils.clear();
        System.out.println(response.getMessage());
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
