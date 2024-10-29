package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

import java.util.List;

public interface UserInfoService {

    ResponseEntity<User> getUser(String token) throws DatabaseAccessException;

    ResponseEntity<AuthBody> login(String name, String password) throws DatabaseAccessException;

    ResponseEntity<AuthBody> register(String name, String password) throws DatabaseAccessException;

    ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password) throws DatabaseAccessException;

    ResponseEntity<?> updatePassword(String name, String newPassword) throws DatabaseAccessException;

    ResponseEntity<?> setRole(String userName, RoleType role) throws DatabaseAccessException;

    ResponseEntity<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ResponseEntity<?> deleteUser(String userName) throws DatabaseAccessException;

    ResponseEntity<RoleType> getUserRole(String userName) throws DatabaseAccessException;

}
