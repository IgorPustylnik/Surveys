package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.edit;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;

import java.util.ArrayList;

public class EditSurveyCommand extends CommandMenu {
    public EditSurveyCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Edit survey";
    }
}
