package ru.vsu.cs.pustylnik_i_v.surveys.servlets.util;

import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ServletUtils {

    private static final Gson gson = new Gson();

    public static User getUser(HttpServletRequest request, HttpServletResponse response, UserService userService) throws DatabaseAccessException {
        Cookie[] cookies = request.getCookies();
        String authToken = null;

        // Trying cookies first
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    authToken = cookie.getValue();
                    break;
                }
            }
        }

        // If no token in cookies, trying Bearer token
        if (authToken == null) {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                authToken = authorizationHeader.substring(7);
            }
        }

        User user = null;

        if (authToken != null) {
            ServiceResponse<User> responseEntity = userService.getUser(authToken);
            if (!responseEntity.success()) {
                Cookie deleteAuthTokenCookie = new Cookie("authToken", null);
                deleteAuthTokenCookie.setMaxAge(0);
                deleteAuthTokenCookie.setPath("/");
                response.addCookie(deleteAuthTokenCookie);
            }
            user = responseEntity.body();
        }

        return user;
    }

    public static Integer getSurveySessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer surveySessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("surveySessionId".equals(cookie.getName())) {
                    surveySessionId = Integer.valueOf(cookie.getValue());
                    break;
                }
            }
        }

        return surveySessionId;
    }

    public static <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        try (Reader reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, clazz);
        }
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

}
