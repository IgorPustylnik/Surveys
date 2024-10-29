package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.admin.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.auth.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.categories.admin.DeleteCategory;
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
        return commands.getOrDefault(type, new UnknownCommand());
    }

    private void mapCommands() {
        commands.put(CommandType.MAIN_MENU, new MainCommandMenu(appContext));

        commands.put(CommandType.LOGIN, new LoginCommand(appContext));
        commands.put(CommandType.REGISTER, new RegisterCommand(appContext));
        commands.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand(appContext));
        commands.put(CommandType.LOGOUT, new LogoutCommand(appContext));

        commands.put(CommandType.LIST_SURVEYS, new ListSurveysCommand(appContext));
        commands.put(CommandType.OPEN_SURVEY, new OpenSurveyCommand(appContext));
        commands.put(CommandType.OPEN_QUESTION, new OpenQuestionCommand(appContext));
        commands.put(CommandType.ADD_QUESTION, new AddQuestionCommand(appContext));
        commands.put(CommandType.NEXT_SURVEYS_PAGE, new NextSurveysPageCommand(appContext));
        commands.put(CommandType.PREVIOUS_SURVEYS_PAGE, new PreviousSurveysPageCommand(appContext));

        commands.put(CommandType.CREATE_SURVEY, new CreateSurveyCommand(appContext));
        commands.put(CommandType.EDIT_SURVEY, new EditSurveyCommand(appContext));
        commands.put(CommandType.SET_SURVEY_CATEGORY, new SetSurveyCategoryCommand(appContext));
        commands.put(CommandType.DELETE_SURVEY, new DeleteSurveyCommand(appContext));

        commands.put(CommandType.LIST_CATEGORIES, new ListAllCategories(appContext));
        commands.put(CommandType.PREVIOUS_CATEGORIES_PAGE, new PreviousCategoriesPageCommand(appContext));
        commands.put(CommandType.NEXT_CATEGORIES_PAGE, new NextCategoriesPageCommand(appContext));
        commands.put(CommandType.OPEN_CATEGORY, new OpenCategoryCommand(appContext));
        commands.put(CommandType.CHOOSE_CATEGORY, new ChooseCategoryCommand(appContext));
        commands.put(CommandType.UNCHOOSE_CATEGORY, new UnchooseCategoryCommand(appContext));
        commands.put(CommandType.DELETE_CATEGORY, new DeleteCategory(appContext));

        commands.put(CommandType.LIST_USERS, new ListUsersCommand(appContext));
        commands.put(CommandType.NEXT_USERS_PAGE, new NextUsersPageCommand(appContext));
        commands.put(CommandType.PREVIOUS_USERS_PAGE, new PreviousUsersPageCommand(appContext));
        commands.put(CommandType.OPEN_USER, new OpenUserCommand(appContext));
        commands.put(CommandType.UPDATE_USER_ROLE, new UpdateUserRoleCommand(appContext));
        commands.put(CommandType.DELETE_USER, new DeleteUserCommand(appContext));

        commands.put(CommandType.UNKNOWN, new UnknownCommand());
        commands.put(CommandType.DATABASE_ERROR, new DatabaseErrorCommand(appContext));
    }
}
