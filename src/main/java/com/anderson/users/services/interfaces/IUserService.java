package com.anderson.users.services.interfaces;

import com.anderson.users.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    User saveUser(User user);
    void deleteUserById(Long id);
}
