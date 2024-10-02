package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.ResponseEntity;


public class LoginCommand extends AppCommand {
    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("your username");
        String password = ConsoleUtils.inputString("your password");

        ResponseEntity<AuthBody> response = appData.getService().login(name, password);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }
        System.out.println(response.getMessage());

        appData.setLocalToken(response.getBody().getToken());
        appData.setLocalUserName(name);
        appData.setLocalRole(response.getBody().getRole());

        ConsoleUtils.clear();
        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
