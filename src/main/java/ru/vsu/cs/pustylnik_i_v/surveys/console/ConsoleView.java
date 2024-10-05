package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.foundation.CommandType;

public class ConsoleView {

    private final ConsoleAppData appData;

    public ConsoleView(ConsoleAppData appData) {
        this.appData = appData;
    }

    public void run() {
//        UserInfoService userInfoService = appData.getUserInfoService();
//        SurveysService surveysService = appData.getSurveysService();
//
//        userInfoService.register("admin", "admin");
//        userInfoService.addRole("admin", RoleType.ADMIN);
//
//        for (int i = 0; i < 4; i++) {
//            surveysService.addSurveyAndGetId("First", "11", "Science");
//            surveysService.addSurveyAndGetId("Second", "22", "Politics");
//            surveysService.addSurveyAndGetId("Third", "3", "Nature");
//        }
//
//        ConsoleUtils.clear();
        appData.getCommandExecutor().getCommand(CommandType.MAIN_MENU).execute();
    }

}
