package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionCommand extends AppCommand {

    public AddQuestionCommand(ConsoleAppContext appContext) {
        super(appContext);
    }

    @Override
    public String getName() {
        return "Add question";
    }

    @Override
    public void execute() {
        Survey survey = appContext.currentSurvey;

        String questionText = ConsoleUtils.inputString("question text");
        if (questionText == null) {
            goBack("Question text is empty", false);
            return;
        }
        Integer optionCount = ConsoleUtils.inputInt("option count");
        if (optionCount == null) {
            goBack("Option count must be a number", false);
            return;
        }
        Integer questionTypeInt = ConsoleUtils.inputInt("question type (1 - single choice, 2 - multiple choice)");
        if (questionTypeInt == null) {
            goBack("Question type must be a number", false);
            return;
        }

        QuestionType questionType;

        if (questionTypeInt == 1) {
            questionType = QuestionType.SINGLE_CHOICE;
        } else if (questionTypeInt == 2) {
            questionType = QuestionType.MULTIPLE_CHOICE;
        } else {
            goBack("Unknown question type", false);
            return;
        }

        List<String> options = new ArrayList<>();

        for (int i = 0; i < optionCount; i++) {
            String optionText = ConsoleUtils.inputString("option " + (i + 1));
            options.add(optionText);
        }
        appContext.getSurveysService().addQuestionToSurvey(survey.getId(), questionText, options, questionType);
        goBack("Question added", true);
    }

    private void goBack(String message, boolean success) {
        ConsoleUtils.clear();
        if (message != null) {
            if (!success) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }
        }
        appContext.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS);
    }

}
