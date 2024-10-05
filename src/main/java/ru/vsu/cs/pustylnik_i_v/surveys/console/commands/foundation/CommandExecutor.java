package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin.AssignAdminCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.ChangePasswordCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.LoginCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.LogoutCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.RegisterCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.AdminCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.AnonymousCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.menus.UserCommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.CreateSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.DeleteSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.edit.EditSurveyCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {

    private final Map<CommandType, Command> commands = new HashMap<>();
    private final ConsoleAppData appData;

    public CommandExecutor(ConsoleAppData appData) {
        this.appData = appData;
        mapCommands();
    }

    public Command getCommand(CommandType type) {
        if (type == CommandType.MAIN_MENU) {
            if (appData.roleType == null) {
                return getCommand(CommandType.ANONYMOUS_MENU);
            }
            switch (appData.roleType) {
                case USER:
                    return getCommand(CommandType.USER_MENU);
                case ADMIN:
                    return getCommand(CommandType.ADMIN_MENU);
            }
        }
        return commands.getOrDefault(type, new UnknownCommand());
    }

    private void mapCommands() {
        commands.put(CommandType.ANONYMOUS_MENU, new AnonymousCommandMenu(appData));
        commands.put(CommandType.USER_MENU, new UserCommandMenu(appData));
        commands.put(CommandType.ADMIN_MENU, new AdminCommandMenu(appData));

        commands.put(CommandType.LOGIN, new LoginCommand(appData));
        commands.put(CommandType.REGISTER, new RegisterCommand(appData));
        commands.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand(appData));
        commands.put(CommandType.LOGOUT, new LogoutCommand(appData));

        commands.put(CommandType.LIST_SURVEYS, new ListSurveysCommand(appData));
        commands.put(CommandType.OPEN_SURVEY, new OpenSurveyCommand(appData));
        commands.put(CommandType.OPEN_QUESTION, new OpenQuestionCommand(appData));
        commands.put(CommandType.NEXT_PAGE, new NextPageCommand(appData));
        commands.put(CommandType.PREVIOUS_PAGE, new PreviousPageCommand(appData));

        commands.put(CommandType.CREATE_SURVEY, new CreateSurveyCommand(appData));
        commands.put(CommandType.EDIT_SURVEY, new EditSurveyCommand(appData));
        commands.put(CommandType.DELETE_SURVEY, new DeleteSurveyCommand(appData));
        commands.put(CommandType.ASSIGN_ADMIN, new AssignAdminCommand(appData));

        commands.put(CommandType.UNKNOWN, new UnknownCommand());
    }
}
