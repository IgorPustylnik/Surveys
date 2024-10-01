package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.DeleteSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.AdminCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.AnonymousCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.UserCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin.AssignAdminCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.CreateSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.ChangePasswordCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.LoginCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.LogoutCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.RegisterCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.edit.EditSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all.*;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static CommandFactory INSTANCE;

    private final Map<CommandType, Command> commands = new HashMap<>();
    private static final ConsoleAppData appData = ConsoleAppData.getInstance();

    public CommandFactory() {
        commands.put(CommandType.ANONYMOUS_MENU, new AnonymousCommandMenu());
        commands.put(CommandType.USER_MENU, new UserCommandMenu());
        commands.put(CommandType.ADMIN_MENU, new AdminCommandMenu());

        commands.put(CommandType.LOGIN, new LoginCommand());
        commands.put(CommandType.REGISTER, new RegisterCommand());
        commands.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand());
        commands.put(CommandType.LOGOUT, new LogoutCommand());

        commands.put(CommandType.LIST_SURVEYS, new ListSurveysCommand());
        commands.put(CommandType.OPEN_SURVEY, new OpenSurveyCommand());
        commands.put(CommandType.START_SURVEY, new StartSurveyCommand());
        commands.put(CommandType.NEXT_PAGE, new NextPageCommand());
        commands.put(CommandType.PREVIOUS_PAGE, new PreviousPageCommand());

        commands.put(CommandType.CREATE_SURVEY, new CreateSurveyCommand());
        commands.put(CommandType.EDIT_SURVEY, new EditSurveyCommand());
        commands.put(CommandType.DELETE_SURVEY, new DeleteSurveyCommand());
        commands.put(CommandType.ASSIGN_ADMIN, new AssignAdminCommand());

        commands.put(CommandType.UNKNOWN, new UnknownCommand());
    }

    public static CommandFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandFactory();
        }
        return INSTANCE;
    }

    public Command getCommand(CommandType type) {
        if (type == CommandType.MAIN_MENU) {
            switch (appData.getLocalRole()) {
                case ANONYMOUS:
                    return new AnonymousCommandMenu();
                case USER:
                    return new UserCommandMenu();
                case ADMIN:
                    return new AdminCommandMenu();
            }
        }
        return commands.getOrDefault(type, new UnknownCommand());
    }
}
