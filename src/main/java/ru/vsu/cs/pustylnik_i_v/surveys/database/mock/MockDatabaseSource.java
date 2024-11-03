package ru.vsu.cs.pustylnik_i_v.surveys.database.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulation;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;

import java.util.Date;

public class MockDatabaseSource {
    public final DBTableSimulation<Answer> answers = new DBTableSimulation<>(
            params -> new Answer(
                    (Integer) params[0],
                    (Integer) params[1])
    );

    public final DBTableSimulation<Category> categories = new DBTableSimulation<>(
            params -> new Category(
                    0,
                    (String) params[0])
    );

    public final DBTableSimulation<Option> options = new DBTableSimulation<>(
            params -> new Option(
                    0,
                    (Integer) params[0],
                    (String) params[1])
    );

    public final DBTableSimulation<Question> questions = new DBTableSimulation<>(
            params -> new Question(
                    0,
                    (Integer) params[0],
                    (String) params[1],
                    (QuestionType) params[2])
    );

    public final DBTableSimulation<Session> sessions = new DBTableSimulation<>(
            params -> new Session(0,
                    (Integer) params[0],
                    (Integer) params[1],
                    (Date) params[2],
                    (Date) params[3])
    );

    public final DBTableSimulation<Survey> surveys = new DBTableSimulation<>(
            params -> new Survey(
                        0,
                        (String) params[0],
                        (String) params[1],
                        (Integer) params[2],
                        (String) params[3],
                        (String) params[4],
                    (Integer) params[5],
                        (Date) params[6]
            )
    );

    public final DBTableSimulation<User> users = new DBTableSimulation<>(
            params -> new User(
                    0,
                    (String) params[0],
                    (RoleType) params[1],
                    (String) params[2])
    );
}
