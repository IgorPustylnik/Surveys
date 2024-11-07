package ru.vsu.cs.pustylnik_i_v.surveys.servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.AuthDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.json.TokenDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletsUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        boolean hasAuthToken = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    hasAuthToken = true;
                    break;
                }
            }
        }

        if (hasAuthToken) {
            response.sendRedirect("/");
        } else {
            request.getRequestDispatcher("/WEB-INF/pages/signup.jsp").forward(request, response);
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
            serviceResponse = userService.register(username, password);
            response.setStatus(serviceResponse.success() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(ServletsUtils.toJson(TokenDTO.of(serviceResponse.message(), serviceResponse.body())));
        } catch (DatabaseAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(e.getMessage());
        }
    }
}