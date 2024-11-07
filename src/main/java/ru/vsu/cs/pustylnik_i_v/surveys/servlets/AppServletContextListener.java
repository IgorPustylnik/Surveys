package ru.vsu.cs.pustylnik_i_v.surveys.servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.ServiceProvider;
import ru.vsu.cs.pustylnik_i_v.surveys.provider.sql.SqlServiceProvider;

@WebListener
public class AppServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ServiceProvider serviceProvider = new SqlServiceProvider();
        sce.getServletContext().setAttribute("userService", serviceProvider.getUserService());
        sce.getServletContext().setAttribute("surveyService", serviceProvider.getSurveyService());
    }

}
