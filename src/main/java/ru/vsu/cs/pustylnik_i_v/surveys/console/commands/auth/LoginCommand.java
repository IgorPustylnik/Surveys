package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;


public class LoginCommand extends AppCommand {

    public LoginCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("your username");
        String password = ConsoleUtils.inputString("your password");

        ServiceResponse<String> response;

        try {
            response = appContext.getUserService().login(name, password);
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
        System.out.println(response.message());

        appContext.setToken(response.body());

        ConsoleUtils.clear();
        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
