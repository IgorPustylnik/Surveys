package ru.vsu.cs.pustylnik_i_v.surveys.services;

import ru.vsu.cs.pustylnik_i_v.surveys.database.enums.RoleType;
import ru.vsu.cs.pustylnik_i_v.surveys.database.entities.User;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.RoleRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.database.repositories.UserRepository;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.RoleNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.exceptions.UserNotFoundException;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.AuthBody;
import ru.vsu.cs.pustylnik_i_v.surveys.services.entities.ResponseEntity;

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

            RoleType roleType = roleRepository.getRole(user.getId()).getRoleType();

            if (!user.getPassword().equals(password)) {
                return new ResponseEntity<>(false, "Wrong password", new AuthBody(roleType, null));
            }

            return new ResponseEntity<>(true, "Login successful", new AuthBody(roleType, null));
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
            userRepository.addUser(name, password);
            try {
                User user = userRepository.getUser(name);
                roleRepository.addRole(user.getId(), RoleType.USER);
            } catch (UserNotFoundException ex) {
                return new ResponseEntity<>(false, "Failed to assign default role", null);
            }
            return new ResponseEntity<>(true, "Registration successful", new AuthBody(RoleType.USER, null));
        }
    }

    @Override
    public ResponseEntity<?> checkIfPasswordIsCorrect(String name, String password) {
        try {
            User user = userRepository.getUser(name);
            if (!user.getPassword().equals(password)) {
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
            user.setPassword(newPassword);
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
}
