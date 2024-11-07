package ru.vsu.cs.pustylnik_i_v.surveys.servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletsUtils;

import java.io.IOException;

@WebFilter(urlPatterns = "/")
public class ActiveSessionFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserService userService = (UserService) context.getAttribute("userService");
        SurveyService surveyService = (SurveyService) context.getAttribute("surveyService");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (((HttpServletRequest) servletRequest).getMethod().equals("GET")) {
            Integer sessionId = ServletsUtils.getSurveySessionId(httpRequest);
            if (sessionId != null) {
                // TODO: get questions to display on a single page (or separately)
                servletRequest.getRequestDispatcher("/WEB-INF/pages/question.jsp").forward(servletRequest, servletResponse);
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}