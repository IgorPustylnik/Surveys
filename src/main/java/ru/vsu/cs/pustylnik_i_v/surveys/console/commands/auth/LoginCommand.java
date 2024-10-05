package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandExecutor;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;


public class LoginCommand extends AppCommand {

    public LoginCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("your username");
        String password = ConsoleUtils.inputString("your password");

        ResponseEntity<AuthBody> response = appData.getUserInfoService().login(name, password);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }
        System.out.println(response.getMessage());

        appData.token = response.getBody().getToken();
        appData.userName = name;
        appData.roleType = response.getBody().getRoleType();

        ConsoleUtils.clear();
        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }
}
