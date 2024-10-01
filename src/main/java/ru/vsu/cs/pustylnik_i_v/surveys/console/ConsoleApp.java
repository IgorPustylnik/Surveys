package ru.vsu.cs.pustylnik_i_v.surveys.console;

import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandFactory;
import ru.vsu.cs.pustylnik_i_v.surveys.console.commands.support.CommandType;
import ru.vsu.cs.pustylnik_i_v.surveys.console.util.ConsoleUtils;
import ru.vsu.cs.pustylnik_i_v.surveys.service.SurveysServiceImpl;

public class ConsoleApp {
    public static void main(String[] args) {
        SurveysServiceImpl.getInstance().register("admin", "admin");
        SurveysServiceImpl.getInstance().addAdmin("admin", "email");

        SurveysServiceImpl.getInstance().addSurvey("First", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Second", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Third", "33", "IT");
        SurveysServiceImpl.getInstance().addSurvey("Fourth", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Fifth", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Sixth", "33", "IT");
        SurveysServiceImpl.getInstance().addSurvey("Seventh", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Eighth", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Ninth", "33", "IT");
        SurveysServiceImpl.getInstance().addSurvey("First", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Second", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Third", "33", "IT");
        SurveysServiceImpl.getInstance().addSurvey("Fourth", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Fifth", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Sixth", "33", "IT");
        SurveysServiceImpl.getInstance().addSurvey("Seventh", "11", "Science");
        SurveysServiceImpl.getInstance().addSurvey("Eighth", "22", "Nature");
        SurveysServiceImpl.getInstance().addSurvey("Ninth", "33", "IT");

        ConsoleUtils.clear();
        CommandFactory.getInstance().getCommand(CommandType.MAIN_MENU).execute();
    }
}
