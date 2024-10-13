package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

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
        String name = ConsoleUtils.inputString("your name");
        String password = ConsoleUtils.inputString("your password");

        ResponseEntity<AuthBody> response = appContext.getUserInfoService().register(name, password);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println(response.getMessage());

        appContext.token = response.getBody().getToken();
        appContext.userName = name;
        appContext.roleType = response.getBody().getRoleType();

        appContext.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
