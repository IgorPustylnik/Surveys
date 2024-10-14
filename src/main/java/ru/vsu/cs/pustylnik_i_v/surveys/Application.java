package ru.vsu.cs.pustylnik_i_v.surveys;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleView;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.mock.MockServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.sql.SqlServiceProvider;

import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        ServiceProvider provider = new SqlServiceProvider();
        ConsoleAppContext consoleAppContext = new ConsoleAppContext(provider.getUserInfoService(), provider.getSurveysService());
        ConsoleView consoleView = new ConsoleView(consoleAppContext);

        ConsoleUtils.clear();
        consoleView.run();
    }
}
