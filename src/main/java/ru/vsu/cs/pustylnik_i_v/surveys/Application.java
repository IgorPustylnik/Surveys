package ru.vsu.cs.pustylnik_i_v.surveys;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppContext;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleView;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.sql.SqlServiceProvider;

public class Application {
    public static void main(String[] args) {
        ServiceProvider provider = new SqlServiceProvider();
        ConsoleAppContext consoleAppContext = new ConsoleAppContext(provider.getUserService(), provider.getSurveyService());
        ConsoleView consoleView = new ConsoleView(consoleAppContext);

        ConsoleUtils.clear();
        consoleView.run();
    }
}
