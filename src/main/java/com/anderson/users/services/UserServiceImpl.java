package com.anderson.users.services;

import com.anderson.users.dto.UserDto;
import com.anderson.users.dto.mapper.DtoMapperUser;
import com.anderson.users.models.Role;
import com.anderson.users.models.User;
import com.anderson.users.repositories.RoleRepository;
import com.anderson.users.repositories.UserRepository;
import com.anderson.users.response.UserResponse;
import com.anderson.users.services.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserResponse> findAllUsers() {
        UserResponse response = new UserResponse();

        try {
            List<User> users = (List<User>) userRepository.findAll();
            if(users.isEmpty()) {
                response.setMetadata("error", "No users found");
            }
            response.getResponse().setUsers(users.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList()));
            response.setMetadata("success", "Users found");
        } catch (Exception e) {
            response.setMetadata("error", "There was an unexpected error");
            e.getStackTrace();
            return new ResponseEntity<UserResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserResponse> findUserById(Long id) {
        UserResponse response = new UserResponse();
        List<UserDto> users = new ArrayList<>();

        try {
            Optional<User> userInDB = userRepository.findById(id);
            if (userInDB.isPresent()) {
                users.add(DtoMapperUser.builder().setUser(userInDB.get()).build());
                response.getResponse().setUsers(users);
                response.setMetadata("success", "User found");
            } else {
                response.setMetadata("error", "The user was not found");
            }
        } catch (Exception e) {
            response.setMetadata("error", "There was an unexpected error");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> saveUser(User user) {
        UserResponse response = new UserResponse();
        List<UserDto> users = new ArrayList<>();
        List<Role> roles = new ArrayList<>();

        String passEncrypted = passwordEncoder.encode(user.getPassword());
        user.setPassword(passEncrypted);

        try {
            // search available roles
            Optional<Role> roleDB = roleRepository.findByName("ROLE_USER");
            if (roleDB.isPresent()) {
                roles.add(roleDB.orElseThrow());
            }
            user.setRoles(roles);

            User userSaved = userRepository.save(user);
            users.add(DtoMapperUser.builder().setUser(userSaved).build());
            response.getResponse().setUsers(users);
            response.setMetadata("success", "User saved");
        } catch (Exception e) {
            response.setMetadata("error", "There was an unexpected error");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> updateUserById(User user, Long id) {
        UserResponse response = new UserResponse();
        List<UserDto> list = new ArrayList<>();

        try {
            // search the user in DB
            Optional<User> userInDB = userRepository.findById(id);

            // check if exist a user identify with the pk
            if(userInDB.isPresent()) {
                // update user
                userInDB.get().setUsername(user.getUsername());
                userInDB.get().setPassword(user.getPassword());

                // save user updated in DB
                User userUpdated = userRepository.save(userInDB.get());

                // add the updated user response (obj) to the response
                list.add(DtoMapperUser.builder().setUser(userUpdated).build());

                // update the response
                response.getResponse().setUsers(list);
                response.setMetadata("success", "User updated");

            } else {
                response.setMetadata("error", "Category not found");
                return new ResponseEntity<UserResponse>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("error", "There was an error updating the category");
            e.getStackTrace();
            return new ResponseEntity<UserResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<UserResponse> deleteUserById(Long id) {
        UserResponse response = new UserResponse();
        try {
            Optional<User> userInDB = userRepository.findById(id);
            if (userInDB.isPresent()) {
                userRepository.deleteById(id);
                response.setMetadata("success", "User deleted");
            } else {
                response.setMetadata("error", "The user was not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("error", "There was an unexpected error");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }
}
