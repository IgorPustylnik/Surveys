package ru.vsu.cs.pustylnik_i_v.surveys.servlets.surveys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.SessionIdDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SessionService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletsUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/survey/*")
public class SurveyServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        SurveyParams params = getSurveyGetParams(request, response, userService, surveyService);
        if (params == null) return;

        if ("edit".equals(params.action())) {
            if (params.user() == null) {
                request.setAttribute("errorMessage", "Not logged in");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                return;
            }
            handleEdit(request, response, params.user(), params.survey());
        } else if (params.action() == null) {
            handleOpen(request, response, params.user(), params.survey());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");
        SessionService sessionService = (SessionService) getServletContext().getAttribute("sessionService");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Survey ID is missing.");
            return;
        }

        String[] pathParts = pathInfo.substring(1).split("/");
        Integer surveyId;

        String action = null;

        try {
            surveyId = Integer.parseInt(pathParts[0]);
            if (pathParts.length > 1) {
                action = pathParts[1];
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid survey ID.");
            return;
        }
        User user;

        try {
            user = ServletsUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
            return;
        }

        if ("start".equals(action)) {
            handleStart(request, response, user, surveyId, sessionService);
        } else if ("delete".equals(action)) {
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }
            handleDelete(request, response, user, surveyId, surveyService);
        }
    }

    // GET

    private void handleOpen(HttpServletRequest request, HttpServletResponse response, User user, Survey survey) throws IOException, ServletException {
        request.setAttribute("user", user);
        request.setAttribute("survey", survey);
        request.getRequestDispatcher("/WEB-INF/pages/survey.jsp").forward(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response, User user, Survey survey) throws IOException, ServletException {
        if (!survey.getAuthorName().equals(user.getName()) && user.getRole() != RoleType.ADMIN) {
            request.setAttribute("errorMessage", "Access denied");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("survey", survey);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/pages/edit_survey.jsp").forward(request, response);
    }

    // POST

    private void handleStart(HttpServletRequest request, HttpServletResponse response, User user, Integer surveyId, SessionService sessionService) throws IOException {
        request.setAttribute("user", user);

        response.setContentType("application/json; charset=UTF-8");

        ServiceResponse<Integer> serviceResponse;

        try {
            serviceResponse = sessionService.startSessionAndGetId(user != null ? user.getId() : null, surveyId);

            if (!serviceResponse.success()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write(serviceResponse.message());
                return;
            }

            response.getWriter().write(ServletsUtils.toJson(SessionIdDTO.of(serviceResponse.body())));
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(e.getMessage());
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, User user, Integer surveyId, SurveyService surveyService) throws IOException {
        Survey survey;
        ServiceResponse<Survey> serviceResponseGet;

        response.setContentType("text/plain; charset=UTF-8");

        try {
            serviceResponseGet = surveyService.getSurvey(surveyId);

            if (!serviceResponseGet.success()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(serviceResponseGet.message());
                return;
            }
            survey = serviceResponseGet.body();
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
            return;
        }

        request.setAttribute("user", user);

        if (!(survey.getAuthorName() != null && survey.getAuthorName().equals(user.getName())) && user.getRole() != RoleType.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Action denied");
            return;
        }

        ServiceResponse<?> serviceResponseDelete;

        try {
            serviceResponseDelete = surveyService.deleteSurvey(survey.getId());

            if (!serviceResponseDelete.success()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(serviceResponseDelete.message());
            }

            response.getWriter().write(serviceResponseDelete.message());
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
        }
    }

    private static SurveyParams getSurveyGetParams(HttpServletRequest request, HttpServletResponse response, UserService userService, SurveyService surveyService) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Survey ID is missing.");
            return null;
        }

        String[] pathParts = pathInfo.substring(1).split("/");
        int surveyId;
        String action = null;

        try {
            surveyId = Integer.parseInt(pathParts[0]);
            if (pathParts.length > 1) {
                action = pathParts[1];
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid survey ID.");
            return null;
        }

        User user;
        try {
            user = ServletsUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return null;
        }

        Survey survey;
        try {
            ServiceResponse<Survey> surveyResponse = surveyService.getSurvey(surveyId);
            if (!surveyResponse.success()) {
                request.setAttribute("errorMessage", surveyResponse.message());
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                return null;
            }
            survey = surveyResponse.body();
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return null;
        }

        return new SurveyParams(user, survey, action);
    }

    private record SurveyParams(User user, Survey survey, String action) {
    }
}