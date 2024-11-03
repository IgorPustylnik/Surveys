package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.QuestionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;

import java.util.List;

public class QuestionMockRepository extends BaseMockRepository implements QuestionRepository {
    public QuestionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<Question> getQuestions(Integer surveyId) {
        return database.questions.get(List.of(
                DBTableSimulationFilter.of(Question::getSurveyId, surveyId))
        );
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type) {
        database.questions.add(surveyId, text, type);
    }
}
