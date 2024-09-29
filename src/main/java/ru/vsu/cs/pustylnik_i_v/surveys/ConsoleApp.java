package ru.vsu.cs.pustylnik_i_v.surveys;

import ru.vsu.cs.pustylnik_i_v.surveys.database.DatabaseService;
import ru.vsu.cs.pustylnik_i_v.surveys.database.MockDatabase;
import ru.vsu.cs.pustylnik_i_v.surveys.entities.Admin;
import ru.vsu.cs.pustylnik_i_v.surveys.enums.QuestionType;

import java.time.Instant;
import java.util.Date;

public class ConsoleApp {
    public static void main(String[] args) throws Exception {
        DatabaseService database = new MockDatabase();
        database.addUser("First one", "1253136");
        System.out.println(database.getUser(0).getName());
        database.addSurvey("Survey 1", null, null, Date.from(Instant.now()));
        database.addQuestion(0,"Question 1", QuestionType.SINGLE);
        database.addOption(0, "First option");
        database.addOption(0, "Second option");
        database.addSession(0, 0, Date.from(Instant.now()), null);
        database.addAnswer(0, 0);
    }
}
