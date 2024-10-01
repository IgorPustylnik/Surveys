package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.service.entities.ResponseEntity;

public class RegisterCommand extends AppCommand {

    @Override
    public String getName() {
        return "Register";
    }

    @Override
    public void execute() {
        String name = ConsoleUtils.inputString("your name");
        String password = ConsoleUtils.inputString("your password");

        ResponseEntity<AuthBody> response = appData.getService().register(name, password);

        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.err.println(response.getMessage());
            CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println(response.getMessage());

        appData.setLocalToken(response.getBody().getToken());
        appData.setLocalUserName(name);
        appData.setLocalRole(response.getBody().getRole());

        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
