package ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.mock.MockDatabaseSource;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.SessionRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.mock.base.BaseMockRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.simulation.DBTableSimulationFilter;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SessionNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.SurveyNotFoundException;

import java.util.Date;
import java.util.List;

public class SessionMockRepository extends BaseMockRepository implements SessionRepository {
    public SessionMockRepository(MockDatabaseSource database) {
        super(database);
    }

    @Override
    public Session getSessionById(int id) {
        return database.sessions.get(List.of(
                DBTableSimulationFilter.of(Session::getId, id)))
                .get(0);
    }

    @Override
    public Session getUserSession(Integer userId) throws SessionNotFoundException {
        List<Session> query = database.sessions.get(List.of(
                DBTableSimulationFilter.of(Session::getUserId, userId))
        );
        if (query.isEmpty()) {
            throw new SessionNotFoundException(-1);
        }
        return query.get(0);
    }

    @Override
    public Integer addSessionAndGetId(int surveyId, Integer userId, Date startedAt, Date finishedAt) throws SurveyNotFoundException {
        List<Survey> query = database.surveys.get(List.of(
                DBTableSimulationFilter.of(Survey::getId, surveyId)));
        if (query.isEmpty()) {
            throw new SurveyNotFoundException(surveyId);
        }        database.sessions.add(surveyId, userId, startedAt, finishedAt);
        return database.sessions.get(List.of(
                DBTableSimulationFilter.of(Session::getStartedAt, startedAt)))
                .get(0).getId();
    }

    @Override
    public void updateSession(Session s) throws SessionNotFoundException {
        List<Session> query = database.sessions.get(List.of(
                DBTableSimulationFilter.of(Session::getId, s.getId())));
        if (query.isEmpty()) {
            throw new SessionNotFoundException(s.getId());
        }
        Session session = query.get(0);
        session.setSurveyId(s.getSurveyId());
        session.setUserId(s.getUserId());
        session.setStartedAt(s.getStartedAt());
        session.setFinishedAt(s.getFinishedAt());
    }

    @Override
    public boolean exists(int id) {
        return database.sessions.contains(List.of(
                DBTableSimulationFilter.of(Session::getId, id))
        );
    }

}
