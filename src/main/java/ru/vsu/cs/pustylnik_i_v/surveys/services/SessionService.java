package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.*;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.*;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.util.Calendar;
import java.util.List;

public class SessionService {

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final SessionRepository sessionRepository;

    public SessionService(UserRepository userRepository,
                         AnswerRepository answerRepository,
                         SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.sessionRepository = sessionRepository;
    }

    public ServiceResponse<Integer> startSessionAndGetId(Integer userId, Integer surveyId) throws DatabaseAccessException {
        if (userId != null) {
            try {
                userId = userRepository.getUser(userId).getId();
            } catch (UserNotFoundException e) {
                return new ServiceResponse<>(false, "User doesn't exist", null);
            }
        }

        Integer sessionId;
        try {
            sessionId = sessionRepository.addSessionAndGetId(surveyId, userId, Calendar.getInstance().getTime(), null);
        } catch (SurveyNotFoundException e) {
            return new ServiceResponse<>(false, "Survey doesn't exist", null);
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
        return new ServiceResponse<>(true, "Successfully created a session", sessionId);
    }

    public ServiceResponse<Session> getSession(int sessionId) throws DatabaseAccessException {
        Session session;

        try {
            session = sessionRepository.getSessionById(sessionId);
            return new ServiceResponse<>(true, "Session found", session);
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        }
    }

    public ServiceResponse<?> submitAnswer(Integer sessionId, Integer optionId) throws DatabaseAccessException {
        try {
            answerRepository.addAnswer(sessionId, optionId);
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        } catch (OptionNotFoundException e) {
            return new ServiceResponse<>(false, "Option doesn't exist", null);
        }
        return new ServiceResponse<>(true, "Answer submitted successfully", null);
    }

    public ServiceResponse<?> submitAnswers(Integer sessionId, List<Integer> optionIds) throws DatabaseAccessException {
        try {
            for (Integer optionId : optionIds) {
                answerRepository.addAnswer(sessionId, optionId);
            }
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        } catch (OptionNotFoundException e) {
            return new ServiceResponse<>(false, "Option doesn't exist", null);
        }
        return new ServiceResponse<>(true, "Answers submitted successfully", null);
    }

    public ServiceResponse<?> finishSession(Integer sessionId) throws DatabaseAccessException {
        try {
            Session session = sessionRepository.getSessionById(sessionId);
            session.setFinishedAt(Calendar.getInstance().getTime());
            sessionRepository.updateSession(session);
            return new ServiceResponse<>(true, "Session finished successfully", null);
        } catch (SessionNotFoundException e) {
            return new ServiceResponse<>(false, "Session doesn't exist", null);
        }
    }
}
