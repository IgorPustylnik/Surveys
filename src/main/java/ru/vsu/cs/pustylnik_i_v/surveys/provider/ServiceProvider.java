package ru.vsu.cs.pustylnik_i_v.surveys.provider;

import ru.vsu.cs.pustylnik_i_v.surveys.services.SurveysService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.TokenValidationService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;

public interface ServiceProvider {

    UserInfoService getUserInfoService();

    SurveysService getSurveysService();

    TokenValidationService getTokenValidationService();

}
