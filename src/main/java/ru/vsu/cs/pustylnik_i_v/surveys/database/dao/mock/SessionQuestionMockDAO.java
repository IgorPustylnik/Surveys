package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.SessionQuestionDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.SessionQuestion;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.util.List;

public class SessionQuestionMockDAO extends BaseMockDAO implements SessionQuestionDAO {
    public SessionQuestionMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<SessionQuestion> getQuestions(int sessionId) throws SessionNotFoundException, DatabaseAccessException {
        return List.of();
    }
}
