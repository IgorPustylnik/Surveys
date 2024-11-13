package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.util.ValidationUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

public class RegisterCommand extends AppCommand {

    public RegisterCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Register";
    }

    @Override
    public void execute() {
        String name, password, validation;
        do {
            name = ConsoleUtils.inputString("your username");
            validation = ValidationUtils.isValidUserName(name);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        do {
            password = ConsoleUtils.inputString("your password");
            validation = ValidationUtils.isValidPassword(password);
            if (validation != null) {
                System.err.println(validation);
            }
        } while (validation != null);

        ServiceResponse<String> response;

        try {
            response = appContext.getUserService().register(name, password);
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

        appContext.setToken(response.body());

        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
