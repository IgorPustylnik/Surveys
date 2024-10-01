package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;

public class StartSurveyCommand extends AppCommand {
    @Override
    public String getName() {
        return "Start";
    }

    @Override
    public void execute() {

        System.out.println(appData.getCurrentSurvey().getName());
        factory.getCommand(CommandType.LIST_SURVEYS).execute();
    }
}
