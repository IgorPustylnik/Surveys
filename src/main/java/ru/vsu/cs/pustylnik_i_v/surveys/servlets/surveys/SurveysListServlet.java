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

@WebServlet(urlPatterns = "/surveys")
public class SurveysListServlet extends HttpServlet {

    private static final int perPageAmount = 4;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        SurveyListParams params = getParams(request, response, userService);
        if (params == null) return;

        ServiceResponse<PagedEntity<List<Survey>>> serviceResponse;

        try {
            serviceResponse = surveyService.getSurveysPagedList(params.categoryId(), params.fromDate(), params.toDate(), params.currentPage() - 1, perPageAmount);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        request.setAttribute("serviceResponseSurveys", serviceResponse);
        request.setAttribute("currentPage", params.currentPage());

        ServiceResponse<PagedEntity<List<Category>>> serviceResponse1;

        try {
            serviceResponse1 = surveyService.getCategoriesPagedList(0, perPageAmount);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        request.setAttribute("serviceResponseCategories", serviceResponse1);

        request.getRequestDispatcher("/WEB-INF/pages/surveys_list.jsp").forward(request, response);
    }

    private SurveyListParams getParams(HttpServletRequest request, HttpServletResponse response, UserService userService) throws ServletException, IOException {
        User user;

        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return null;
        }

        request.setAttribute("user", user);

        String pageParam = request.getParameter("page");
        String categoryIdParam = request.getParameter("categoryId");
        String fromDateParam = request.getParameter("fromDate");
        String toDateParam = request.getParameter("toDate");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = null;
        Date toDate = null;

        try {
            if (fromDateParam != null && !fromDateParam.isEmpty()) {
                fromDate = dateFormat.parse(fromDateParam);
            }
            if (toDateParam != null && !toDateParam.isEmpty()) {
                toDate = dateFormat.parse(toDateParam);
            }
        } catch (ParseException e) {
            getServletContext().log(e.getMessage());
        }

        int currentPage = Optional.ofNullable(pageParam)
                .filter(param -> !param.isEmpty())
                .map(Integer::parseInt)
                .orElse(1);

        Integer categoryId = Optional.ofNullable(categoryIdParam)
                .filter(param -> !param.isEmpty())
                .map(Integer::parseInt)
                .orElse(null);
        return new SurveyListParams(user, fromDate, toDate, currentPage, categoryId);
    }

    private record SurveyListParams(User user, Date fromDate, Date toDate, int currentPage, Integer categoryId) {
    }

}