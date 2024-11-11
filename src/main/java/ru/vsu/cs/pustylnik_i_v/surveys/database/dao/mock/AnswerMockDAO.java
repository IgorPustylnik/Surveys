package ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.*;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.AnswerDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.dao.mock.base.BaseMockDAO;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.OptionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AnswerMockDAO extends BaseMockDAO implements AnswerDAO {

    public AnswerMockDAO(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public void putAnswersToQuestion(int sessionId, List<Integer> optionIds) throws SessionNotFoundException, OptionNotFoundException {
        List<DBTableSimulationFilter<Session>> filterSessions = new ArrayList<>();
        filterSessions.add(DBTableSimulationFilter.of(s -> s.getId() == sessionId));
        if (!database.sessions.contains(filterSessions)) {
            throw new SessionNotFoundException(sessionId);
        }

        int questionId = getQuestionIdByOption(optionIds.get(0));
        if (questionId == -1) {
            throw new OptionNotFoundException(optionIds.get(0));
        }

        List<DBTableSimulationFilter<Option>> filterOptions = new ArrayList<>();
        filterOptions.add(DBTableSimulationFilter.of(o -> o.getQuestionId() == questionId));
        List<Option> queryOptions = database.options.get(filterOptions);
        if (queryOptions.isEmpty()) {
            throw new OptionNotFoundException(optionIds.get(0));
        }

        Option option = queryOptions.get(0);

        List<DBTableSimulationFilter<Question>> filterQuestions = new ArrayList<>();
        filterQuestions.add(DBTableSimulationFilter.of(q -> q.getOptions().contains(option)));
        List<Question> queryQuestions = database.questions.get(filterQuestions);
        if (queryQuestions.isEmpty()) {
            throw new OptionNotFoundException(optionIds.get(0));
        }

        Question question = queryQuestions.get(0);

        List<Option> optionsToRemove = question.getOptions();
        List<Integer> optionsToRemoveIds = optionsToRemove.stream().map(Option::getId).toList();

        List<DBTableSimulationFilter<Answer>> filterAnswers = new ArrayList<>();
        filterAnswers.add(DBTableSimulationFilter.of(a -> a.getSessionId() == sessionId));
        filterAnswers.add(DBTableSimulationFilter.of(a -> optionsToRemoveIds.contains(a.getOptionId())));
        database.answers.remove(filterAnswers);

        for (Integer optionId : optionIds) {
            filterOptions = new ArrayList<>();
            filterOptions.add(DBTableSimulationFilter.of(o -> o.getId() == optionId));
            if (!database.options.contains(filterOptions)) {
                throw new OptionNotFoundException(optionId);
            }

            database.answers.add(sessionId, optionId);
        }
    }

    private int getQuestionIdByOption(int optionId) throws OptionNotFoundException {
        List<DBTableSimulationFilter<Option>> filterOptions = new ArrayList<>();
        filterOptions.add(DBTableSimulationFilter.of(o -> o.getId() == optionId));
        List<Option> foundOptions = database.options.get(filterOptions);

        if (foundOptions.isEmpty()) {
            throw new OptionNotFoundException(optionId);
        }

        return foundOptions.get(0).getQuestionId();
    }

    @Override
    public boolean exists(int sessionId, int optionId) {
        List<DBTableSimulationFilter<Answer>> filters = new ArrayList<>();
        filters.add(DBTableSimulationFilter.of(a -> a.getSessionId() == sessionId));
        filters.add(DBTableSimulationFilter.of(a -> a.getOptionId() == optionId));

        return database.answers.contains(filters);
    }

}
