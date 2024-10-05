package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandMenu;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenQuestionCommand extends CommandMenu {
    public OpenQuestionCommand(ConsoleAppData appData) {
        super(new ArrayList<>(), appData);
    }

    @Override
    public String getName() {
        return "Start";
    }

    @Override
    public void execute() {
        Survey currentSurvey = appData.currentSurvey;
        String userName = appData.userName;

        // Starting a session if needed
        if (appData.currentSessionId == null) {
            ResponseEntity<Integer> response = appData.getSurveysService().startSessionAndGetId(userName, currentSurvey.getId());

            if (!response.isSuccess()) {
                System.err.println("Failed to start the survey");
            }
            Integer sessionId = response.getBody();
            appData.currentSessionId = sessionId;
        }

        Integer currentQuestionIndex = appData.currentQuestionIndex;
        ResponseEntity<PagedEntity<Question>> response = appData.getSurveysService().getQuestionPagedEntity(currentSurvey.getId(), currentQuestionIndex);

        // Questions not found
        if (!response.isSuccess()) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
            return;
        }

        // No more questions
        PagedEntity<Question> questionPagedEntity = response.getBody();
        if (questionPagedEntity == null) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
            return;
        }

        Question question = questionPagedEntity.getPage();

        setTitle(String.format("%s", question.getText()));

        ResponseEntity<List<Option>> response1 = appData.getSurveysService().getQuestionOptionList(question.getId());

        if (!response1.isSuccess()) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
            return;
        }

        List<Option> options = response1.getBody();
        if (options == null) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            appData.getCommandExecutor().getCommand(CommandType.LIST_SURVEYS).execute();
            return;
        }

        printMenu(options
                .stream()
                .map(Option::getDescription)
                .collect(Collectors.toList()));

        QuestionType questionType = question.getType();

        List<Integer> input = ConsoleUtils.inputIntList(
                String.format("your answer (%s)", questionType == QuestionType.SINGLE_CHOICE ? "single number" : "multiple numbers divided by spaces"));

        if (input.isEmpty()) {
            System.err.println("Please enter your answer");
            ConsoleUtils.clear();
            this.execute();
            return;
        }
        if (questionType == QuestionType.SINGLE_CHOICE && input.size() > 1) {
            System.err.println("Must be a single choice");
        }

        input.forEach(index ->
                appData.getSurveysService().submitAnswer(appData.currentSessionId, options.get(index).getId())
        );
        appData.currentQuestionIndex += 1;

        ConsoleUtils.clear();
        this.execute();
    }


}
