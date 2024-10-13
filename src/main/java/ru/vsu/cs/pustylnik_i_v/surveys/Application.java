package ru.vsu.cs.pustylnik_i_v.surveys;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleView;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.mock.MockServiceProvider;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        ServiceProvider provider = new MockServiceProvider();
        ConsoleAppContext consoleAppContext = new ConsoleAppContext(provider.getUserInfoService(), provider.getSurveysService());
        ConsoleView consoleView = new ConsoleView(consoleAppContext);

        consoleAppContext.getUserInfoService().register("admin", "admin");
        consoleAppContext.getUserInfoService().setRole("admin", RoleType.ADMIN);

        Survey survey = consoleAppContext.getSurveysService().addSurveyAndGetSelf("test", "desc", "Science").getBody();
        consoleAppContext.getSurveysService().addQuestionToSurvey(survey.getId(), "Q1", List.of("first", "second"), QuestionType.SINGLE_CHOICE);
        consoleAppContext.getSurveysService().addQuestionToSurvey(survey.getId(), "Q2", List.of("first", "second", "third"), QuestionType.MULTIPLE_CHOICE);

        ConsoleUtils.clear();
        consoleView.run();
    }
}
