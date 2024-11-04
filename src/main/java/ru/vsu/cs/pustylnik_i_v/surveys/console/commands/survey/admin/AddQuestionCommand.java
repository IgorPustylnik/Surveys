package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.admin;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.AppCommand;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;

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

        String validation;

        String questionText;
        do {
            questionText = ConsoleUtils.inputString("question text");
            validation = (questionText == null || questionText.isEmpty()) ? "Question text is empty" : null;
            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        Integer optionCount;
        do {
            optionCount = ConsoleUtils.inputInt("option count");
            validation = (optionCount == null || optionCount < 1) ? "Option count must be a positive number" : null;

            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        Integer questionTypeInt;
        QuestionType questionType = null;
        do {
            questionTypeInt = ConsoleUtils.inputInt("question type (1 - single choice, 2 - multiple choice)");
            validation = (questionTypeInt == null) ? "Question type must be a number (1 - single choice, 2 - multiple choice)" : null;
            if (questionTypeInt != null) {
                if (questionTypeInt == 1) {
                    questionType = QuestionType.SINGLE_CHOICE;
                } else if (questionTypeInt == 2) {
                    questionType = QuestionType.MULTIPLE_CHOICE;
                } else {
                    validation = "Question type must be 1 or 2";
                }
            }
            if (validation != null) {
                ConsoleUtils.clear();
                System.err.println(validation);
            }
        } while (validation != null);

        List<String> options = new ArrayList<>();

        for (int i = 0; i < optionCount; i++) {
            String optionText;
            do {
                optionText = ConsoleUtils.inputString("option " + (i + 1));
                validation = (optionText == null || optionText.isEmpty()) ? "Option text is empty" : null;
                if (validation != null) {
                    ConsoleUtils.clear();
                    System.err.println(validation);
                }
            } while (validation != null);
            options.add(optionText);
        }

        try {
            appContext.getSurveyService().addQuestionToSurvey(survey.getId(), questionText, options, questionType);
        } catch (
                DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        ConsoleUtils.clear();
        System.out.println("Question added");
        appContext.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS);
    }
}
