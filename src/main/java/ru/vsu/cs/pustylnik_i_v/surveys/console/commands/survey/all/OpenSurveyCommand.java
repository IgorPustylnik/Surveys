package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;

import java.util.ArrayList;

public class OpenSurveyCommand extends CommandMenu {

    public OpenSurveyCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Open survey";
    }

    @Override
    public void execute() {
        commands = new ArrayList<>();

        commands.add(CommandType.OPEN_QUESTION);

        if (appContext.localUser != null && appContext.localUser.getRole() == RoleType.ADMIN) {
            commands.add(CommandType.EDIT_SURVEY);
            commands.add(CommandType.SET_SURVEY_CATEGORY);
            commands.add(CommandType.DELETE_SURVEY);
        }
        commands.add(CommandType.LIST_SURVEYS);
        Survey s = appContext.currentSurvey;

        setTitle(String.format("Survey name: %s\nCategory: %s\nDescription: %s\nAuthor: %s",
                s.getName(),
                s.getCategoryName(),
                s.getDescription(),
                s.getAuthorName()));

        super.execute();
    }

}
