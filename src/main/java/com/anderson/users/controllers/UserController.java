package com.anderson.users.controllers;

import com.anderson.users.models.User;
import com.anderson.users.response.UserResponse;
import com.anderson.users.services.interfaces.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of User objects representing all users
     */
    @GetMapping("/users")
    public ResponseEntity<UserResponse> findAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be retrieved
     * @return a ResponseEntity containing the user if found, or a 404 Not Found status if the user does not exist
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    /**
     * Saves a new user in the system.
     *
     * @param user the user object to be saved
     * @return a ResponseEntity containing the saved user object and a CREATED HTTP status
     */
    @PostMapping("/users")
    public ResponseEntity<UserResponse> saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    /**
     * Updates an existing user's information.
     *
     * @param user the user object containing the updated user details
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user, @PathVariable Long id) {
        return userService.updateUserById(user, id);
    }
}
