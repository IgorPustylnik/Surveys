package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.CryptoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.TokenValidationService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public class TokenValidationServiceImpl implements TokenValidationService {

    private final CryptoService cryptoService;
    private final UserInfoService userInfoService;

    public TokenValidationServiceImpl(CryptoService cryptoService, UserInfoService userInfoService) {
        this.cryptoService = cryptoService;
        this.userInfoService = userInfoService;
    }

    @Override
    public ResponseEntity<RoleType> getRoleFromToken(String token) {
        try {
            User user = (User) cryptoService.decrypt(token);
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
