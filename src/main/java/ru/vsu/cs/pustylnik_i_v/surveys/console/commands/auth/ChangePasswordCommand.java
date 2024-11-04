package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

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

        ServiceResponse<?> response;

        String newPassword, validation;
        do {
            newPassword = ConsoleUtils.inputString("your new password");
            validation = ValidationUtils.isValidPassword(newPassword);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        try {
            response = appContext.getUserService().updatePassword(appContext.localUser.getName(), oldPassword, newPassword);
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        if (!response.success()) {
            ConsoleUtils.clear();
            System.err.println(response.message());
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println(response.message());
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
