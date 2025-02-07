package com.anderson.users.controllers;

import com.anderson.users.models.User;
import com.anderson.users.services.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be retrieved
     * @return a ResponseEntity containing the user if found, or a 404 Not Found status if the user does not exist
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Saves a new user in the system.
     *
     * @param user the user object to be saved
     * @return a ResponseEntity containing the saved user object and a CREATED HTTP status
     */
    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> userDB = userService.findUserById(id);
        if(userDB.isPresent()) {
            userService.deleteUserById(userDB.get().getId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Updates an existing user's information.
     *
     * @param user the user object containing the updated user details
     */
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Optional<User> userDB = userService.findUserById(user.getId());
        if(userDB.isPresent()) {
            User userUpdated = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
        }

        return ResponseEntity.notFound().build();
    }

    // ALL THIS BUSINESS LOGIC SHOULD BE IN SERVICE LAYER.
    // TODO: UPDATE LATER
}
