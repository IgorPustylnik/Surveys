package ru.vsu.cs.pustylnik_i_v.surveys.servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;
import ru.vsu.cs.pustylnik_i_v.surveys.servlets.util.ServletUtils;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class BanFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserService userService = (UserService) context.getAttribute("userService");
        try {
            User user = ServletUtils.getUser((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, userService);
            if (user != null && user.getRole() == RoleType.BANNED) {
                if (((HttpServletRequest) servletRequest).getMethod().equals("POST")) {
                    ServletUtils.sendError((HttpServletResponse) servletResponse, HttpServletResponse.SC_FORBIDDEN, "You have been banned");
                    return;
                }
                if (((HttpServletRequest) servletRequest).getMethod().equals("GET")) {
                    String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();

                    if (!requestUri.startsWith("/static/")) {
                        servletRequest.setAttribute("user", user);
                        servletRequest.getRequestDispatcher("WEB-INF/pages/banned.jsp").forward(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        } catch (DatabaseAccessException ignored) {
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}