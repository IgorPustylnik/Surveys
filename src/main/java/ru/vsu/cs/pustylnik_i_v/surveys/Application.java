package ru.vsu.cs.pustylnik_i_v.surveys;

import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleAppData;
import ru.vsu.cs.pustylnik_i_v.surveys.console.ConsoleView;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.mock.MockServiceProvider;

public class Application {
    public static void main(String[] args) {
        ServiceProvider provider = new MockServiceProvider();
        ConsoleAppData consoleAppData = new ConsoleAppData(provider.getUserInfoService(), provider.getSurveysService());
        consoleAppData.getUserInfoService().register("admin", "admin");
        consoleAppData.getUserInfoService().setRole("admin", RoleType.ADMIN);
        ConsoleView consoleView = new ConsoleView(consoleAppData);
        ConsoleUtils.clear();
        consoleView.run();
    }
}
