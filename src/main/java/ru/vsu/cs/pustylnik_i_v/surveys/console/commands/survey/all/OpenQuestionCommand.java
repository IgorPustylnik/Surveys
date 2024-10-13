package ru.vsu.cs.pustylnik_i_v.surveys.console.commands.survey.all;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
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
        // Starting a session if needed
        if (appContext.currentSessionId == null) {
            ResponseEntity<Integer> response = appContext.getSurveysService().startSessionAndGetId(appContext.userName, appContext.currentSurvey.getId());

            if (!response.isSuccess()) {
                System.err.println(response.getMessage());
                goBack();
                return;
            }
            appContext.currentSessionId = response.getBody();
        }

        ResponseEntity<PagedEntity<Question>> response = appContext.getSurveysService().getQuestionPagedEntity(appContext.currentSurvey.getId(), appContext.currentQuestionIndex);

        // Questions not found
        PagedEntity<Question> questionPagedEntity = response.getBody();
        if (!response.isSuccess() || questionPagedEntity == null) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            goBack();
            return;
        }

        Question question = questionPagedEntity.getPage();

        setTitle(String.format("%s", question.getText()));

        ResponseEntity<List<Option>> response1 = appContext.getSurveysService().getQuestionOptionList(question.getId());

        if (!response1.isSuccess()) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            goBack();
            return;
        }

        List<Option> options = response1.getBody();
        if (options == null) {
            ConsoleUtils.clear();
            System.out.println(response.getMessage());
            goBack();
            return;
        }

        printMenu(options
                .stream()
                .map(Option::getDescription)
                .collect(Collectors.toList()));

        QuestionType questionType = question.getType();

        List<Integer> input = ConsoleUtils.inputIntList(
                String.format("your answer (%s)", questionType == QuestionType.SINGLE_CHOICE ? "single number" : "multiple numbers divided by spaces"));

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
                appContext.getSurveysService().submitAnswer(appContext.currentSessionId, options.get(index).getId())
        );
        appContext.currentQuestionIndex += 1;

        ConsoleUtils.clear();
        this.execute();
    }

    private void goBack() {
        appContext.getSurveysService().finishSession(appContext.currentSessionId);
        appContext.currentSessionId = null;
        appContext.currentQuestionIndex = 0;
        appContext.getCommandExecutor().getCommand(CommandType.OPEN_SURVEY).execute();
    }

}
