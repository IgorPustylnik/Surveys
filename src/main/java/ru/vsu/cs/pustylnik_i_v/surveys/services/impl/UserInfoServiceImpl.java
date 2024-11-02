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
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;
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
    public ResponseEntity<User> getUser(String token) throws DatabaseAccessException {
        User user;
        int userId;
        try {
            TokenInfo tokenInfo = (TokenInfo) AESCryptoUtil.getInstance().decrypt(token);
            userId = tokenInfo.getId();
        } catch (Exception e) {
            return new ResponseEntity<>(false, "Failed to decrypt token", null);
        }
        try {
            user = userRepository.getUser(userId);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User not found", null);
        }
        return new ResponseEntity<>(true, "Successfully found user", user);
    }

    @Override
    public ResponseEntity<AuthBody> login(String name, String password) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(name);
            String hashedPassword = user.getPassword();

            RoleType roleType = user.getRole();

            if (!HashingUtil.passwordMatch(password, hashedPassword)) {
                return new ResponseEntity<>(false, "Wrong password", null);
            }

            String token;
            TokenInfo tokenInfo = new TokenInfo(user.getId());

            try {
                token = AESCryptoUtil.getInstance().encrypt(tokenInfo);
            } catch (Exception e) {
                return new ResponseEntity<>(false, "Failed to generate a token", null);
            }

            return new ResponseEntity<>(true, "Login successful", new AuthBody(roleType, token));
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "No such user", null);
        } catch (RoleNotFoundException e) {
            return new ResponseEntity<>(false, "User's role not found", null);
        }
    }

    @Override
    public ResponseEntity<AuthBody> register(String name, String password) throws DatabaseAccessException {
        try {
            userRepository.getUser(name);
            return new ResponseEntity<>(false, "Username is taken", null);
        } catch (UserNotFoundException e) {
            userRepository.addUser(name, RoleType.USER, HashingUtil.hashPassword(password));
            User user;
            try {
                user = userRepository.getUser(name);
            } catch (UserNotFoundException ex) {
                return new ResponseEntity<>(false, "Failed to add user", null);
            }

            String token;
            TokenInfo tokenInfo = new TokenInfo(user.getId());

            try {
                token = AESCryptoUtil.getInstance().encrypt(tokenInfo);
            } catch (Exception ex) {
                return new ResponseEntity<>(false, "Failed to generate a token", null);
            }

            return new ResponseEntity<>(true, "Registration successful", new AuthBody(RoleType.USER, token));
        }
    }

    @Override
    public ResponseEntity<?> updatePassword(String name, String oldPassword, String newPassword) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(name);
            String oldHashedPassword = user.getPassword();
            if (!HashingUtil.passwordMatch(oldPassword, oldHashedPassword)) {
                return new ResponseEntity<>(false, "Old password is incorrect", null);
            }
            String newPasswordHashed = HashingUtil.hashPassword(newPassword);
            user.setPassword(newPasswordHashed);
            userRepository.updateUser(user);
            return new ResponseEntity<>(true, "Password changed", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<?> setRole(String userName, RoleType role) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(userName);
            user.setRole(role);
            userRepository.updateUser(user);
            return new ResponseEntity<>(true, "Role set successfully", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount) throws DatabaseAccessException {
        PagedEntity<List<User>> usersPagedEntity = userRepository.getUsersPagedList(page, perPageAmount);
        if (usersPagedEntity.getPage().isEmpty()) {
            return new ResponseEntity<>(false, "Users not found", usersPagedEntity);
        }
        return new ResponseEntity<>(true, "Users successfully found", usersPagedEntity);
    }

    @Override
    public ResponseEntity<?> deleteUser(String userName) throws DatabaseAccessException {
        userRepository.deleteUser(userName);
        return new ResponseEntity<>(true, "User successfully deleted", null);
    }

    @Override
    public ResponseEntity<RoleType> getUserRole(String userName) throws DatabaseAccessException {
        try {
            User user = userRepository.getUser(userName);
            RoleType roleType = user.getRole();
            return new ResponseEntity<>(true, "Successfully found user's role", roleType);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User's role doesn't exist", null);
        }
    }
}
