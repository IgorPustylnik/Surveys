package ru.vsu.cs.pustylnik_i_v.surveys.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.SessionQuestion;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.AnswersDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/session/*")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");
        SessionService sessionService = (SessionService) getServletContext().getAttribute("sessionService");

        User user;

        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        String pathInfo = request.getPathInfo();

        String[] pathParts = pathInfo.substring(1).split("/");
        int sessionId;

        try {
            sessionId = Integer.parseInt(pathParts[0]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid session ID.");
            return;
        }

        Session session;

        ServiceResponse<Session> serviceResponseSession;
        try {
            serviceResponseSession = sessionService.getSession(sessionId);

            if (!serviceResponseSession.success()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found.");
                return;
            }

            session = serviceResponseSession.body();
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Invalid session ID.");
            return;
        }

        Integer cookieSessionId;
        if (user != null) {
            if (session.getUserId() != null && session.getUserId() != user.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
        } else {
            cookieSessionId = ServletUtils.getSurveySessionId(request);
            if (cookieSessionId == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
            if (cookieSessionId != session.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
        }

        if (session.getFinishedAt() != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/pages/thanks.jsp").forward(request, response);
            return;
        }

        List<SessionQuestion> questions;

        ServiceResponse<List<SessionQuestion>> serviceResponseQuestions;
        try {
            serviceResponseQuestions = sessionService.getQuestions(session.getId());

            if (!serviceResponseQuestions.success()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Survey not found.");
                return;
            }

            questions = serviceResponseQuestions.body();
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Couldn't retrieve questions from database.");
            return;
        }

        String questionIndexParam = request.getParameter("question");
        Integer questionIndex = (questionIndexParam != null) ? Integer.parseInt(questionIndexParam) : null;
        if (questionIndex == null) {
            response.sendRedirect(String.format("/session/%d?question=1", sessionId));
            return;
        }
        if (questionIndex > questions.size()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Question not found.");
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("questions", questions);
        request.setAttribute("questionIndex", questionIndex);
        request.setAttribute("sessionId", sessionId);
        request.setAttribute("surveyName", session.getSurveyName());
        request.getRequestDispatcher("/WEB-INF/pages/survey_taking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SessionService sessionService = (SessionService) getServletContext().getAttribute("sessionService");

        response.setContentType("text/plain; charset=UTF-8");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        String pathInfo = request.getPathInfo();

        String[] pathParts = pathInfo.substring(1).split("/");
        int sessionId;

        try {
            sessionId = Integer.parseInt(pathParts[0]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid session ID.");
            return;
        }

        Session session;
        ServiceResponse<Session> serviceResponseCheckSession;

        try {
            serviceResponseCheckSession = sessionService.getSession(sessionId);

            if (!serviceResponseCheckSession.success()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_NOT_FOUND, serviceResponseCheckSession.message());
                return;
            }

            session = serviceResponseCheckSession.body();
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        Integer cookieSessionId;
        if (user != null) {
            if (session.getUserId() != null && session.getUserId() != user.getId()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
        } else {
            cookieSessionId = ServletUtils.getSurveySessionId(request);
            if (cookieSessionId == null) {
                ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
            if (cookieSessionId != session.getId()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
        }

        String action;
        if (pathParts.length > 1) {
            action = pathParts[1];
            if (action.equals("submit")) {
                handleSubmit(request, response, sessionService, sessionId);
                return;
            } else if (action.equals("finish")) {
                handleFinish(response, sessionService, sessionId);
                return;
            } else {
                ServletUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                return;
            }
        }

        response.getWriter().write("Success");
    }

    private static void handleFinish(HttpServletResponse response, SessionService sessionService, int sessionId) throws IOException {
        ServiceResponse<?> serviceResponseFinish;
        try {
            serviceResponseFinish = sessionService.finishSession(sessionId);

            if (!serviceResponseFinish.success()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseFinish.message());
            }

        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    private static void handleSubmit(HttpServletRequest request, HttpServletResponse response, SessionService sessionService, int sessionId) throws IOException {
        AnswersDTO answersDTO = ServletUtils.parseJson(request, AnswersDTO.class);

        if (answersDTO == null) {
            ServletUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid answers json.");
            return;
        }
        List<Integer> options = answersDTO.options();

        ServiceResponse<?> serviceResponseSubmit;

        try {
            serviceResponseSubmit = sessionService.submitAnswers(sessionId, options);

            if (!serviceResponseSubmit.success()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseSubmit.message());
            }

        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
}
