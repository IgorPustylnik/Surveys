package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.TokenValidationService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.util.AESCryptoUtil;

public class TokenValidationServiceImpl implements TokenValidationService {

    private final UserInfoService userInfoService;

    public TokenValidationServiceImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public ServiceResponse<RoleType> getRoleFromToken(String token) {
        try {
            User user = (User) AESCryptoUtil.getInstance().decrypt(token);
            ServiceResponse<RoleType> response = userInfoService.getUserRole(user.getName());
            if (!response.success()) {
                return new ServiceResponse<>(false, response.message(), null);
            }
            RoleType roleType = response.body();
            return new ServiceResponse<>(true, response.message(), roleType);
        } catch (Exception e) {
            return new ServiceResponse<>(false, "Token decryption failed", null);
        }
    }
}
