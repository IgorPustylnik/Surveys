package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

public interface UserInfoService {

    ResponseEntity<AuthBody> login(String name, String password);

    ResponseEntity<AuthBody> register(String name, String password);

    ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password);

    ResponseEntity<?> updatePassword(String name, String newPassword);

    ResponseEntity<?> setRole(String userName, RoleType role);

}
