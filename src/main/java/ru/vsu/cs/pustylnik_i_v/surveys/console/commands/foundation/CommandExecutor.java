package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.edit.EditSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

    private final Map<CommandType, Command> commands = new HashMap<>();
    private final ConsoleAppContext appContext;

    public CommandExecutor(ConsoleAppContext appContext) {
        this.appContext = appContext;
        mapCommands();
    }

    public Command getCommand(CommandType type) {
        if (type == CommandType.MAIN_MENU) {
            if (appContext.roleType == null) {
                return getCommand(CommandType.ANONYMOUS_MENU);
            }
            switch (appContext.roleType) {
                case USER:
                    return getCommand(CommandType.USER_MENU);
                case ADMIN:
                    return getCommand(CommandType.ADMIN_MENU);
            }
        }
        return commands.getOrDefault(type, new UnknownCommand());
    }

    private void mapCommands() {
        commands.put(CommandType.ANONYMOUS_MENU, new AnonymousCommandMenu(appContext));
        commands.put(CommandType.USER_MENU, new UserCommandMenu(appContext));
        commands.put(CommandType.ADMIN_MENU, new AdminCommandMenu(appContext));

        commands.put(CommandType.LOGIN, new LoginCommand(appContext));
        commands.put(CommandType.REGISTER, new RegisterCommand(appContext));
        commands.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand(appContext));
        commands.put(CommandType.LOGOUT, new LogoutCommand(appContext));

        commands.put(CommandType.LIST_SURVEYS, new ListSurveysCommand(appContext));
        commands.put(CommandType.OPEN_SURVEY, new OpenSurveyCommand(appContext));
        commands.put(CommandType.OPEN_QUESTION, new OpenQuestionCommand(appContext));
        commands.put(CommandType.ADD_QUESTION, new AddQuestionCommand(appContext));
        commands.put(CommandType.NEXT_PAGE, new NextPageCommand(appContext));
        commands.put(CommandType.PREVIOUS_PAGE, new PreviousPageCommand(appContext));

        commands.put(CommandType.CREATE_SURVEY, new CreateSurveyCommand(appContext));
        commands.put(CommandType.EDIT_SURVEY, new EditSurveyCommand(appContext));
        commands.put(CommandType.DELETE_SURVEY, new DeleteSurveyCommand(appContext));

        commands.put(CommandType.LIST_USERS, new ListUsersCommand(appContext));
        commands.put(CommandType.NEXT_USERS_PAGE, new NextUsersPageCommand(appContext));
        commands.put(CommandType.PREVIOUS_USERS_PAGE, new PreviousUsersPageCommand(appContext));
        commands.put(CommandType.OPEN_USER, new OpenUserCommand(appContext));
        commands.put(CommandType.DELETE_USER, new DeleteUserCommand(appContext));

        commands.put(CommandType.UNKNOWN, new UnknownCommand());
    }
}
