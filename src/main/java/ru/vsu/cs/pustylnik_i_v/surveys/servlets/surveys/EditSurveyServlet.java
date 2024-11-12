package ru.vsu.cs.pustylnik_i_v.surveys.servlets.surveys;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.json.EditSurveyDTO;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;

@WebServlet(urlPatterns = "/surveys/edit")
public class EditSurveyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = (UserService) getServletContext().getAttribute("userService");
        SurveyService surveyService = (SurveyService) getServletContext().getAttribute("surveyService");

        response.setContentType("application/json; charset=utf-8");

        User user;
        try {
            user = ServletUtils.getUser(request, response, userService);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        if (user == null) {
            ServletUtils.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        EditSurveyDTO editSurveyDTO = ServletUtils.parseJson(request, EditSurveyDTO.class);

        ServiceResponse<?> serviceResponse;
        try {
            serviceResponse = surveyService.updateSurvey(editSurveyDTO);
        } catch (DatabaseAccessException e) {
            ServletUtils.sendError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, e.getMessage());
            return;
        }

        if (!serviceResponse.success()) {
            ServletUtils.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, serviceResponse.message());
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Success");
    }
}
