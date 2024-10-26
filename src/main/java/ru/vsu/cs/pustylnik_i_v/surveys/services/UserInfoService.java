package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.List;

public interface UserInfoService {

    ResponseEntity<AuthBody> login(String name, String password);

    ResponseEntity<AuthBody> register(String name, String password);

    ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password);

    ResponseEntity<?> updatePassword(String name, String newPassword);

    ResponseEntity<?> setRole(String userName, RoleType role);

    ResponseEntity<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount);

    ResponseEntity<?> deleteUser(String userName);

    ResponseEntity<RoleType> getUserRole(String userName);

}
