package ru.vsu.cs.pustylnik_i_v.surveys.servlets.surveys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.QuestionType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.QuestionDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.SurveyDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.SurveyIdDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/surveys/create")
public class CreateSurveyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        request.setAttribute("user", user);

        List<Category> categories;

        ServiceResponse<PagedEntity<List<Category>>> serviceResponse;
        try {
            serviceResponse = surveyService.getCategoriesPagedList(0, 100);
            if (!serviceResponse.success()) {
                request.setAttribute("errorMessage", serviceResponse.message());
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                return;
            }
            categories = serviceResponse.body().page();
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/pages/create_survey.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        response.setContentType("application/json; charset=utf-8");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
            return;
        }

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        SurveyDTO surveyDTO = ServletUtils.parseJson(request, SurveyDTO.class);

        ServiceResponse<Integer> serviceResponse;
        try {
            serviceResponse = addSurvey(surveyDTO, user, surveyService);
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().write(e.getMessage());
            return;
        }

        if (!serviceResponse.success()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().write(serviceResponse.message());
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(ServletUtils.toJson(SurveyIdDTO.of(serviceResponse.body())));
    }

    private ServiceResponse<Integer> addSurvey(SurveyDTO surveyDTO, User user, SurveyService surveyService) throws DatabaseAccessException {
        String name = surveyDTO.name();
        String description = surveyDTO.description();
        String categoryName = surveyDTO.category();

        if (categoryName.isEmpty()) {
            categoryName = null;
        }

        ServiceResponse<Survey> serviceResponseSurvey = surveyService.addSurveyAndGetSelf(name, description, categoryName, user.getId());

        if (!serviceResponseSurvey.success()) {
            return new ServiceResponse<>(false, serviceResponseSurvey.message(), null);
        }

        Integer surveyId = serviceResponseSurvey.body().getId();

        for (QuestionDTO questionDTO : surveyDTO.questions()) {
            String questionDescription = questionDTO.description();
            String questionTypeString = questionDTO.type();
            QuestionType questionType = QuestionType.valueOf(questionTypeString);
            List<String> options = questionDTO.options();

            surveyService.addQuestionToSurvey(surveyId, questionDescription, options, questionType);
        }

        return new ServiceResponse<>(true, "Successfully created a survey", surveyId);
    }
}