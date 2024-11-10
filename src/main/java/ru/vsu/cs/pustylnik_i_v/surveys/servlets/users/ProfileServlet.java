package ru.vsu.cs.pustylnik_i_v.surveys.servlets.users;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

@WebServlet(urlPatterns = "/profile/*")
public class ProfileServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        UserService userService = (UserService) getServletContext().getAttribute("userService");

        ProfileParams params;
        try {
            params = getProfileParams(request, response, userService);
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", "Access denied");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        if (params == null) return;


        if (params.userSelf().getId() == params.user.getId()) {
            handleSelf(request, response, params.userSelf());
        } else if (params.userSelf().getRole() == RoleType.ADMIN) {
            handleAdmin(request, response, params.userSelf(), params.user());
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void handleSelf(HttpServletRequest request, HttpServletResponse response, User user) throws
            IOException, ServletException {
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/pages/profile_self.jsp").forward(request, response);
    }

    private void handleAdmin(HttpServletRequest request, HttpServletResponse response, User userSelf, User user) throws
            IOException, ServletException {
        request.setAttribute("user", userSelf);
        request.setAttribute("userView", user);
        request.setAttribute("roles", Arrays.stream(RoleType.values())
                .filter(r -> r != RoleType.BANNED)
                .toArray(RoleType[]::new));

        request.getRequestDispatcher("/WEB-INF/pages/profile_admin.jsp").forward(request, response);
    }

    private static ProfileParams getProfileParams(HttpServletRequest request, HttpServletResponse
            response, UserService userService) throws IOException, DatabaseAccessException {
        User userSelf;

        userSelf = ServletUtils.getUser(request, response, userService);

        if (userSelf == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendRedirect(request.getContextPath() + "/profile/" + userSelf.getId());
            return null;
        }

        String[] pathParts = pathInfo.substring(1).split("/");
        int userId;

        try {
            userId = Integer.parseInt(pathParts[0]);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
            return null;
        }

        User user;

        user = userService.getUser(userId).body();

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return new ProfileParams(userSelf, user);
    }

    private record ProfileParams(User userSelf, User user) {
    }
}
