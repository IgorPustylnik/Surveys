package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.TokenValidationService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.util.AESCryptoUtil;

public class TokenValidationServiceImpl implements TokenValidationService {

    private final UserInfoService userInfoService;

    public TokenValidationServiceImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public ResponseEntity<RoleType> getRoleFromToken(String token) {
        try {
            User user = (User) AESCryptoUtil.getInstance().decrypt(token);
            ResponseEntity<RoleType> response = userInfoService.getUserRole(user.getName());
            if (!response.isSuccess()) {
                return new ResponseEntity<>(false, response.getMessage(), null);
            }
            RoleType roleType = response.getBody();
            return new ResponseEntity<>(true, response.getMessage(), roleType);
        } catch (Exception e) {
            return new ResponseEntity<>(false, "Token decryption failed", null);
        }
    }
}
