package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class RegisterCommand extends AppCommand {

    public RegisterCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Register";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("your name");
        String password = ConsoleUtils.inputString("your password");

        ResponseEntity<AuthBody> response = appData.getUserInfoService().register(name, password);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println(response.getMessage());

        appData.token = response.getBody().getToken();
        appData.userName = name;
        appData.roleType = response.getBody().getRoleType();

        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
