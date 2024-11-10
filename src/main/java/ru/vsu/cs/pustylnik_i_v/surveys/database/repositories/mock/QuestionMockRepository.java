package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Option;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.QuestionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;

import java.util.ArrayList;
import java.util.List;

public class QuestionMockRepository extends BaseMockRepository implements QuestionRepository {
    public QuestionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public List<Question> getQuestions(int surveyId) {
        List<DBTableSimulationFilter<Question>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(q -> q.getSurveyId() == surveyId));

        return database.questions.get(filters);
    }

    @Override
    public void addQuestion(int surveyId, String text, QuestionType type, List<String> options) {
        List<DBTableSimulationFilter<Survey>> filtersSurvey = new ArrayList<>();
        filtersSurvey.add(DBTableSimulationFilter.of(s -> s.getId() == surveyId));

        List<Survey> query = database.surveys.get(filtersSurvey);
        if (query.isEmpty()) {
            return;
        }

        List<Option> optionsList = new ArrayList<>();
        for (String optionString: options) {
            int optionId = database.options.add(surveyId, optionString);
            optionsList.add(new Option(optionId, surveyId, optionString));
        }

        database.questions.add(surveyId, text, type, optionsList);

        Survey survey = query.get(0);
        survey.setQuestionsAmount(survey.getQuestionsAmount() + 1);
    }

    @Override
    public void deleteQuestion(int id) {
        List<DBTableSimulationFilter<Question>> filterQuestion = new ArrayList<>();
        filterQuestion.add(DBTableSimulationFilter.of(q -> q.getId() == id));

        Question question = database.questions.get(filterQuestion).get(0);

        List<DBTableSimulationFilter<Survey>> filtersSurvey = new ArrayList<>();
        filtersSurvey.add(DBTableSimulationFilter.of(s -> s.getId() == question.getSurveyId()));
        List<Survey> query = database.surveys.get(filtersSurvey);

        if (query.isEmpty()) {
            return;
        }
        Survey survey = query.get(0);
        survey.setQuestionsAmount(survey.getQuestionsAmount() - 1);

        List<DBTableSimulationFilter<Option>> filtersOption = new ArrayList<>();
        filtersOption.add(DBTableSimulationFilter.of(o -> o.getQuestionId() == id));
        database.options.remove(filtersOption);

        database.questions.remove(filterQuestion);
    }
}
