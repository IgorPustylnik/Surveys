package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;

public class DeleteSurveyCommand extends AppCommand {

    public DeleteSurveyCommand(ConsoleAppData appData) {
        super(appData);
    }

    @Override
    public String getName() {
        return "Delete survey";
    }

    @Override
    public void execute() {

    }
}
