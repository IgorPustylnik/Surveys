package ru.vsu.cs.pustylnik_i_v.surveys.provider;

import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveyService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserService;

public interface ServiceProvider {

    UserService getUserService();

    SurveyService getSurveyService();

}
