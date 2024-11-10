package ru.vsu.cs.pustylnik_i_v.surveys.servlets.users;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");

        Params params = getParams(request, response, userService);
        if (params == null) return;

        switch (params.action()) {
            case "update-role":
                String roleTypeString = request.getParameter("role");
                handleRoleUpdate(response, params.userId(), roleTypeString, userService);
                break;
            case "delete":
                handleDeletion(response, params.userId(), userService);
                break;
            case "toggle-ban":
                handleToggleBan(response, params.userId(), userService);
        }
    }

    private void handleRoleUpdate(HttpServletResponse response, Integer userId, String roleTypeString, UserService userService)
            throws IOException {
        roleTypeString = roleTypeString.toUpperCase();
        RoleType newRole = RoleType.valueOf(roleTypeString);

        User user;
        ServiceResponse<User> serviceResponseGet;
        try {
            serviceResponseGet = userService.getUser(userId);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }
        if (!serviceResponseGet.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseGet.message());
            return;
        }
        user = serviceResponseGet.body();

        if (user.getRole() == RoleType.ADMIN) {
            ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Admin role cannot be updated");
            return;
        }

        ServiceResponse<?> serviceResponseSet;
        try {
            serviceResponseSet = userService.setRole(user.getName(), newRole);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }

        if (!serviceResponseSet.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseSet.message());
            return;
        }

        response.getWriter().write(serviceResponseSet.message());
    }

    private void handleDeletion(HttpServletResponse response, Integer userId, UserService userService)
            throws IOException {
        User user;
        ServiceResponse<User> serviceResponseGet;
        try {
            serviceResponseGet = userService.getUser(userId);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        if (!serviceResponseGet.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseGet.message());
            return;
        }
        user = serviceResponseGet.body();

        if (user.getRole() == RoleType.ADMIN) {
            ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Admin cannot be deleted");
            return;
        }

        ServiceResponse<?> serviceResponseDelete;
        try {
            serviceResponseDelete = userService.deleteUser(user.getName());
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }

        if (!serviceResponseDelete.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseDelete.message());
            return;
        }

        response.getWriter().write(serviceResponseDelete.message());
    }

    private void handleToggleBan(HttpServletResponse response, Integer userId, UserService userService)
            throws IOException {
        User user;
        ServiceResponse<User> serviceResponseGet;
        try {
            serviceResponseGet = userService.getUser(userId);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }
        if (!serviceResponseGet.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseGet.message());
            return;
        }
        user = serviceResponseGet.body();

        if (user.getRole() == RoleType.ADMIN) {
            ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Admin cannot be banned");
            return;
        }

        ServiceResponse<?> serviceResponseDelete;
        try {
            serviceResponseDelete = userService.toggleBanUser(user.getName());
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }

        if (!serviceResponseDelete.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponseDelete.message());
            return;
        }

        response.getWriter().write(serviceResponseDelete.message());
    }

    private static Params getParams(HttpServletRequest request, HttpServletResponse response, UserService userService) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            ServletUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Survey ID is missing.");
            return null;
        }

        String[] pathParts = pathInfo.substring(1).split("/");
        int userId;

        String action = null;

        try {
            userId = Integer.parseInt(pathParts[0]);
            if (pathParts.length > 1) {
                action = pathParts[1];
            }
            if (action == null) {
                return null;
            }
        } catch (NumberFormatException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
            return null;
        }

        User userSelf;
        try {
            userSelf = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return null;
        }

        if (userSelf == null || userSelf.getRole() != RoleType.ADMIN) {
            ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return null;
        }
        return new Params(userId, action);
    }

    private record Params(Integer userId, String action) {
    }
}