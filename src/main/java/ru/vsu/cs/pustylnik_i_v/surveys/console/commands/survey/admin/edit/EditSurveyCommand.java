package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin.edit;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;

import java.util.ArrayList;

public class EditSurveyCommand extends CommandMenu {
    public EditSurveyCommand(ConsoleAppData appData) {
        super(new ArrayList<>(), appData);
    }

    @Override
    public String getName() {
        return "Edit survey";
    }
}
