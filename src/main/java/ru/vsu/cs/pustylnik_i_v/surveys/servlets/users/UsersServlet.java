package ru.vsu.cs.pustylnik_i_v.surveys.servlets.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.Category;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/users")
public class UsersServlet extends HttpServlet {
    private static final int perPageAmount = 4;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        SurveyListParams params = getParams(request, response, userService);
        if (params == null) return;

        if (params.user().getRole() != RoleType.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        ServiceResponse<PagedEntity<List<User>>> serviceResponse;

        try {
            serviceResponse = userService.getUsersPagedList(params.currentPage() - 1, perPageAmount);
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

        request.getRequestDispatcher("/WEB-INF/pages/users_list.jsp").forward(request, response);
    }

    private SurveyListParams getParams(HttpServletRequest request, HttpServletResponse response, UserService userService) throws ServletException, IOException {
        User user;

        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return null;
        }

        if (user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
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
