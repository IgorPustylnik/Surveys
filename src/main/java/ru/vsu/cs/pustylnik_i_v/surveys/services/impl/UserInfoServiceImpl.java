package ru.vsu.cs.pustylnik_i_v.surveys.services.impl;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.RoleRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.RoleNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.UserInfoService;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.PagedEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;
import ru.vsu.cs.pustylnik_i_v.surveys.util.HashingUtil;

import java.util.List;

public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserInfoServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<AuthBody> login(String name, String password) {
        try {
            User user = userRepository.getUser(name);
            String hashedPassword = user.getPassword();

            RoleType roleType = roleRepository.getRole(user.getId()).getRoleType();

            if (!HashingUtil.passwordMatch(password, hashedPassword)) {
                return new ResponseEntity<>(false, "Wrong password", null);
            }

            String token;

            try {
                token = AESCryptoService.getInstance().encrypt(user);
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
    public ResponseEntity<AuthBody> register(String name, String password) {
        try {
            userRepository.getUser(name);
            return new ResponseEntity<>(false, "Username exists", null);
        } catch (UserNotFoundException e) {
            userRepository.addUser(name, HashingUtil.hashPassword(password));
            User user;
            try {
                user = userRepository.getUser(name);
                roleRepository.addRole(user.getId(), RoleType.USER);
            } catch (UserNotFoundException ex) {
                return new ResponseEntity<>(false, "Failed to assign default role", null);
            }

            String token;

            try {
                token = AESCryptoService.getInstance().encrypt(user);
            } catch (Exception ex) {
                return new ResponseEntity<>(false, "Failed to generate a token", null);
            }

            return new ResponseEntity<>(true, "Registration successful", new AuthBody(RoleType.USER, token));
        }
    }

    @Override
    public ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password) {
        try {
            User user = userRepository.getUser(name);
            String hashedPassword = user.getPassword();
            if (!HashingUtil.passwordMatch(password, hashedPassword)) {
                return new ResponseEntity<>(false, "Password is incorrect", null);
            }
            return new ResponseEntity<>(true, "Password is correct", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<?> updatePassword(String name, String newPassword) {
        try {
            User user = userRepository.getUser(name);
            String hashedPassword = HashingUtil.hashPassword(newPassword);
            user.setPassword(hashedPassword);
            userRepository.updateUser(user);
            return new ResponseEntity<>(true, "Password changed", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<?> setRole(String userName, RoleType role) {
        try {
            User user = userRepository.getUser(userName);
            int id = user.getId();
            if (roleRepository.exists(id)) {
                roleRepository.updateRole(id, role);
            } else {
                roleRepository.addRole(id, role);
            }
            return new ResponseEntity<>(true, "Role set successfully", null);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User doesn't exist", null);
        }
    }

    @Override
    public ResponseEntity<PagedEntity<List<User>>> getUsersPagedList(Integer page, Integer perPageAmount) {
        PagedEntity<List<User>> usersPagedEntity = userRepository.getUsersPagedList(page, perPageAmount);
        if (usersPagedEntity.getPage().isEmpty()) {
            return new ResponseEntity<>(false, "Users not found", usersPagedEntity);
        }
        return new ResponseEntity<>(true, "Users successfully found", usersPagedEntity);
    }

    @Override
    public ResponseEntity<?> deleteUser(String userName) {
        userRepository.deleteUser(userName);
        return new ResponseEntity<>(true, "User successfully deleted", null);
    }

    @Override
    public ResponseEntity<RoleType> getUserRole(String userName) {
        try {
            User user = userRepository.getUser(userName);
            RoleType roleType = roleRepository.getRole(user.getId()).getRoleType();
            return new ResponseEntity<>(true, "Successfully found user's role", roleType);

        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(false, "User's role doesn't exist", null);
        }
    }
}
