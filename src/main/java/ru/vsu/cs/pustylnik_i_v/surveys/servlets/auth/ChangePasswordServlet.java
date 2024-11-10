package ru.vsu.cs.pustylnik_i_v.surveys.servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.ChangePasswordDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/change-password")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        UserService userService = (UserService) getServletContext().getAttribute("userService");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher("WEB-INF/pages/change_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");

        response.setContentType("text/plain; charset=UTF-8");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        if (user == null) {
            ServletUtils.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        String oldPassword;
        String newPassword;

        ChangePasswordDTO changePasswordDTO = ServletUtils.parseJson(request, ChangePasswordDTO.class);
        oldPassword = changePasswordDTO.oldPassword();
        newPassword = changePasswordDTO.newPassword();

        ServiceResponse<?> serviceResponse;

        try {
            serviceResponse = userService.updatePassword(user.getName(), oldPassword, newPassword);
            if (!serviceResponse.success()) {
                ServletUtils.sendError(response, HttpServletResponse.SC_FORBIDDEN, serviceResponse.message());
                return;
            }
            response.getWriter().write(serviceResponse.message());
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
        }
    }
}