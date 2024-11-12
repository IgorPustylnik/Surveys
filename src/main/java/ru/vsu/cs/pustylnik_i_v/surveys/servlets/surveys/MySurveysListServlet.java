package ru.vsu.cs.pustylnik_i_v.surveys.servlets.surveys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Survey;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/surveys/my")
public class MySurveysListServlet extends HttpServlet {

    private static final int perPageAmount = 4;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        SurveyListParams params = getParams(request, response, userService);
        if (params == null) return;
        ServiceResponse<PagedEntity<List<Survey>>> serviceResponse;

        if (params.user == null) {
            response.sendRedirect("/surveys");
            return;
        }

        try {
            serviceResponse = surveyService.getSurveysPagedList(params.user().getName(), null, null, null, params.currentPage() - 1, perPageAmount);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        request.setAttribute("serviceResponseSurveys", serviceResponse);
        request.setAttribute("currentPage", params.currentPage());

        request.getRequestDispatcher("/WEB-INF/pages/my_surveys_list.jsp").forward(request, response);
    }

    private SurveyListParams getParams(HttpServletRequest request, HttpServletResponse response, UserService userService) throws IOException {
        User user;

        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return null;
        }

        request.setAttribute("user", user);

        String pageParam = request.getParameter("page");

        int currentPage = Optional.ofNullable(pageParam)
                .filter(param -> !param.isEmpty())
                .map(Integer::parseInt)
                .orElse(1);
        return new SurveyListParams(user, currentPage);
    }

    private record SurveyListParams(User user, int currentPage) {
    }

}