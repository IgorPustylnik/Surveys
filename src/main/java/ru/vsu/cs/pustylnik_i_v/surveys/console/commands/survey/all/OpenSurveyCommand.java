package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;

import java.util.ArrayList;

public class OpenSurveyCommand extends CommandMenu {

    public OpenSurveyCommand(ConsoleAppData appData) {
        super(new ArrayList<>(), appData);
    }

    @Override
    public String getName() {
        return "Open survey";
    }

    @Override
    public void execute() {
        commands.add(CommandType.OPEN_QUESTION);

        if (appData.roleType == RoleType.ADMIN) {
            commands.add(CommandType.EDIT_SURVEY);
            commands.add(CommandType.DELETE_SURVEY);
        }
        commands.add(CommandType.LIST_SURVEYS);
        Survey s = appData.currentSurvey;

        // TODO: should be category name, not id
        setTitle(String.format("Survey name: %s\nCategory id: %d\nDescription: %s", s.getName(), s.getCategoryId(), s.getDescription()));
        super.execute();
    }

}
