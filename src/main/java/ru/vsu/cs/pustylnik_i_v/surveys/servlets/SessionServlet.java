package ru.vsu.cs.pustylnik_i_v.surveys.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Question;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Session;
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
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
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

        if (session.getUserId() != user.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        if (session.getFinishedAt() != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/pages/thanks.jsp").forward(request, response);
            return;
        }

        List<Question> questions;

        ServiceResponse<List<Question>> serviceResponseQuestions;
        try {
            serviceResponseQuestions = surveyService.getQuestions(session.getSurveyId());

            if (!serviceResponseQuestions.success()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Survey not found.");
                return;
            }

            questions = serviceResponseQuestions.body();
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Couldn't retrieve questions from database.");
            return;
        }

        request.setAttribute("user", user);
        request.setAttribute("questions", questions);
        request.setAttribute("sessionId", sessionId);
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
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
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
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(serviceResponseCheckSession.message());
                return;
            }

            session = serviceResponseCheckSession.body();
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
            return;
        }

        if (session.getUserId() != user.getId()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied.");
            return;
        }

        String action;
        if (pathParts.length > 1) {
            action = pathParts[1];
            if (!action.equals("submit")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        AnswersDTO answersDTO = ServletUtils.parseJson(request, AnswersDTO.class);

        if (answersDTO == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid answers json.");
            return;
        }
        List<Integer> options = answersDTO.options();

        ServiceResponse<?> serviceResponseSubmit;

        try {
            serviceResponseSubmit = sessionService.submitAnswers(sessionId, options);

            if (!serviceResponseSubmit.success()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(serviceResponseSubmit.message());
                return;
            }

        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
            return;
        }

        ServiceResponse<?> serviceResponseFinish;
        try {
            serviceResponseFinish = sessionService.finishSession(sessionId);

            if (!serviceResponseFinish.success()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(serviceResponseFinish.message());
            }

        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
        }

        response.getWriter().write("Success");
    }
}
