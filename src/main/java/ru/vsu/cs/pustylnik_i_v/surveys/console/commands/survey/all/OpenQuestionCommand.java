package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpenQuestionCommand extends CommandMenu {
    public OpenQuestionCommand(ConsoleAppContext appContext) {
        super(new ArrayList<>(), appContext);
    }

    @Override
    public String getName() {
        return "Start";
    }

    @Override
    public void execute() {
        User localUser = appContext.localUser();
        Survey currentSurvey = appContext.currentSurvey();

        // Starting a session if needed
        if (appContext.currentSessionId == null) {
            ServiceResponse<Integer> response;

            try {
                response = appContext.getSurveyService().startSessionAndGetId(localUser.getName(), currentSurvey.getId());
            } catch (DatabaseAccessException e) {
                appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
                return;
            }

            if (!response.success()) {
                System.err.println(response.message());
                goBack();
                return;
            }
            appContext.currentSessionId = response.body();
        }


        ServiceResponse<PagedEntity<Question>> response;

        try {
            response = appContext.getSurveyService().getQuestionPagedEntity(currentSurvey.getId(), appContext.currentQuestionIndex);
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
            return;
        }

        // Questions not found
        PagedEntity<Question> questionPagedEntity = response.body();
        if (!response.success() || questionPagedEntity == null) {
            ConsoleUtils.clear();
            System.out.println(response.message());
            goBack();
            return;
        }

        Question question = questionPagedEntity.page();

        setTitle(String.format("%s", question.getText()));


        List<Option> options = question.getOptions();
        if (options.isEmpty()) {
            ConsoleUtils.clear();
            System.err.println("Error: question has no options");
            goBack();
            return;
        }

        printMenu(options
                .stream()
                .map(Option::getDescription)
                .collect(Collectors.toList()));

        QuestionType questionType = question.getType();

        List<Integer> input = ConsoleUtils.inputIntList(
                String.format("your answer (%s)", questionType == QuestionType.SINGLE_CHOICE ? "single number" : "one or multiple numbers divided by spaces"));

        if (input.isEmpty()
                || (questionType == QuestionType.SINGLE_CHOICE && input.size() > 1)
                || input.stream().anyMatch(num -> num > options.size() - 1)) {
            System.err.println("Invalid answer");
            ConsoleUtils.clear();
            this.execute();
            return;
        }

        Set<Integer> inputSet = new HashSet<>(input);
        inputSet.forEach(index ->
                {
                    try {
                        appContext.getSurveyService().submitAnswer(appContext.currentSessionId, options.get(index).getId());
                    } catch (DatabaseAccessException e) {
                        appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
                    }
                }
        );
        appContext.currentQuestionIndex += 1;

        ConsoleUtils.clear();
        this.execute();
    }

    private void goBack() {
        try {
            appContext.getSurveyService().finishSession(appContext.currentSessionId);
            appContext.currentSessionId = null;
            appContext.currentQuestionIndex = 0;
            appContext.getCommandExecutor().getCommand(CommandType.OPEN_SURVEY).execute();
        } catch (DatabaseAccessException e) {
            appContext.getCommandExecutor().getCommand(CommandType.DATABASE_ERROR).execute();
        }
    }

}
