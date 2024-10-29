package ru.vsu.cs.pustylnik_i_v.surveys.database.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.emulation.DBTableImitation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

import java.util.Date;

public class MockDatabaseSource {
    public final DBTableImitation<Answer> answers = new DBTableImitation<>(
            params -> new Answer(
                    (Integer) params[0],
                    (Integer) params[1])
    );

    public final DBTableImitation<Category> categories = new DBTableImitation<>(
            params -> new Category(
                    0,
                    (String) params[0])
    );

    public final DBTableImitation<Option> options = new DBTableImitation<>(
            params -> new Option(
                    0,
                    (Integer) params[0],
                    (String) params[1])
    );

    public final DBTableImitation<Question> questions = new DBTableImitation<>(
            params -> new Question(
                    0,
                    (Integer) params[0],
                    (String) params[1],
                    (QuestionType) params[2])
    );

    public final DBTableImitation<Session> sessions = new DBTableImitation<>(
            params -> new Session(0,
                    (Integer) params[0],
                    (Integer) params[1],
                    (Date) params[2],
                    (Date) params[3])
    );

    public final DBTableImitation<Survey> surveys = new DBTableImitation<>(
            params -> new Survey(
                    0,
                    (String) params[0],
                    (String) params[1],
                    (Integer) params[2],
                    (String) params[3],
                    (Date) params[4])
    );

    public final DBTableImitation<User> users = new DBTableImitation<>(
            params -> new User(
                    0,
                    (String) params[0],
                    (RoleType) params[1],
                    (String) params[2])
    );
}
