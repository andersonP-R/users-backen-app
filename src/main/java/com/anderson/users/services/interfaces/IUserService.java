package com.anderson.users.services.interfaces;

import com.anderson.users.models.User;
import com.anderson.users.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<UserResponse> findAllUsers();
    ResponseEntity<UserResponse> findUserById(Long id);
    ResponseEntity<UserResponse> saveUser(User user);
    ResponseEntity<UserResponse> updateUserById(User user, Long id);
    ResponseEntity<UserResponse> deleteUserById(Long id);
}
