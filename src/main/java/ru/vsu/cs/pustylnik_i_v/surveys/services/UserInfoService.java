package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;

import java.util.List;

public interface UserInfoService {

    ServiceResponse<User> getUser(String token) throws DatabaseAccessException;

    ServiceResponse<User> getUser(Integer id) throws DatabaseAccessException;

    ServiceResponse<AuthBody> login(String name, String password) throws DatabaseAccessException;

    ServiceResponse<AuthBody> register(String name, String password) throws DatabaseAccessException;

    ServiceResponse<?> updatePassword(String name, String oldPassword, String newPassword) throws DatabaseAccessException;

    ServiceResponse<?> setRole(String userName, RoleType role) throws DatabaseAccessException;

    ServiceResponse<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException;

    ServiceResponse<?> deleteUser(String userName) throws DatabaseAccessException;

    ServiceResponse<RoleType> getUserRole(String userName) throws DatabaseAccessException;

}
