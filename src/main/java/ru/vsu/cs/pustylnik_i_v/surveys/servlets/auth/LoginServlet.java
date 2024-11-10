package ru.vsu.cs.pustylnik_i_v.surveys.servlets.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.AuthDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.TokenDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletsUtils;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");

        User user;
        try {
            user = ServletsUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        if (user != null) {
            response.sendRedirect("/");
        } else {
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");

        response.setContentType("application/json; charset=UTF-8");

        String username;
        String password;

        AuthDTO authDTO = ServletsUtils.parseJson(request, AuthDTO.class);
        username = authDTO.username();
        password = authDTO.password();

        ServiceResponse<String> serviceResponse;

        try {
            serviceResponse = userService.login(username, password);
            if (serviceResponse.success()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(ServletsUtils.toJson(TokenDTO.of(serviceResponse.message(), serviceResponse.body())));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write(serviceResponse.message());
            }
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(e.getMessage());
        }
    }
}