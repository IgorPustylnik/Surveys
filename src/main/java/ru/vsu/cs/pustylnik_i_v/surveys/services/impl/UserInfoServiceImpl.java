package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.DatabaseAccessException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.RoleNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ServiceResponse;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.TokenInfo;
import ru.vsu.cs.pustylnik_i_v.surveys.util.AESCryptoUtil;
import ru.vsu.cs.pustylnik_i_v.surveys.util.HashingUtil;

import java.util.List;

public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    public UserInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ServiceResponse<User> getUser(String token) throws DatabaseAccessException {
        User user;
        int userId;
        try {
            TokenInfo tokenInfo = (TokenInfo) AESCryptoUtil.getInstance().decrypt(token);
            userId = tokenInfo.id();
        } catch (Exception e) {
            return new ServiceResponse<>(false, "Failed to decrypt token", null);
        }
        try {
            user = userRepository.getUser(userId);
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User not found", null);
        }
        return new ServiceResponse<>(true, "Successfully found user", user);
    }

    @Override
    public ServiceResponse<User> getUser(Integer id) throws DatabaseAccessException {
        User user;
        try {
            user = userRepository.getUser(id);
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User not found", null);
        }
        return new ServiceResponse<>(true, "Successfully found user", user);
    }

    @Override
    public ServiceResponse<AuthBody> login(String name, String password) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(name);
            String hashedPassword = user.getPassword();

            RoleType roleType = user.getRole();

            if (!HashingUtil.passwordMatch(password, hashedPassword)) {
                return new ServiceResponse<>(false, "Wrong password", null);
            }

            String token;
            TokenInfo tokenInfo = new TokenInfo(user.getId());

            try {
                token = AESCryptoUtil.getInstance().encrypt(tokenInfo);
            } catch (Exception e) {
                return new ServiceResponse<>(false, "Failed to generate a token", null);
            }

            return new ServiceResponse<>(true, "Login successful", new AuthBody(roleType, token));
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "No such user", null);
        } catch (RoleNotFoundException e) {
            return new ServiceResponse<>(false, "User's role not found", null);
        }
    }

    @Override
    public ServiceResponse<AuthBody> register(String name, String password) throws DatabaseAccessException {
        try {
            userRepository.getUser(name);
            return new ServiceResponse<>(false, "Username is taken", null);
        } catch (UserNotFoundException e) {
            userRepository.addUser(name, RoleType.USER, HashingUtil.hashPassword(password));
            User user;
            try {
                user = userRepository.getUser(name);
            } catch (UserNotFoundException ex) {
                return new ServiceResponse<>(false, "Failed to add user", null);
            }

            String token;
            TokenInfo tokenInfo = new TokenInfo(user.getId());

            try {
                token = AESCryptoUtil.getInstance().encrypt(tokenInfo);
            } catch (Exception ex) {
                return new ServiceResponse<>(false, "Failed to generate a token", null);
            }

            return new ServiceResponse<>(true, "Registration successful", new AuthBody(RoleType.USER, token));
        }
    }

    @Override
    public ServiceResponse<?> updatePassword(String name, String oldPassword, String newPassword) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(name);
            String oldHashedPassword = user.getPassword();
            if (!HashingUtil.passwordMatch(oldPassword, oldHashedPassword)) {
                return new ServiceResponse<>(false, "Old password is incorrect", null);
            }
            String newPasswordHashed = HashingUtil.hashPassword(newPassword);
            user.setPassword(newPasswordHashed);
            userRepository.updateUser(user);
            return new ServiceResponse<>(true, "Password changed", null);
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ServiceResponse<?> setRole(String userName, RoleType role) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(userName);
            user.setRole(role);
            userRepository.updateUser(user);
            return new ServiceResponse<>(true, "Role set successfully", null);
        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ServiceResponse<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        PagedEntity<List<User>> usersPagedEntity = userRepository.getUsersPagedList(page, perPageAmount);
        if (usersPagedEntity.page().isEmpty()) {
            return new ServiceResponse<>(false, "Users not found", usersPagedEntity);
        }
        return new ServiceResponse<>(true, "Users successfully found", usersPagedEntity);
    }

    @Override
    public ServiceResponse<?> deleteUser(String userName) throws DatabaseAccessException {
        userRepository.deleteUser(userName);
        return new ServiceResponse<>(true, "User successfully deleted", null);
    }

    @Override
    public ServiceResponse<RoleType> getUserRole(String userName) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(userName);
            RoleType roleType = user.getRole();
            return new ServiceResponse<>(true, "Successfully found user's role", roleType);

        } catch (UserNotFoundException e) {
            return new ServiceResponse<>(false, "User's role doesn't exist", null);
        }
    }
}
