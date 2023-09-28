package ua.dtsebulia.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.testassignment.exception.UserAlreadyExistsException;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.service.UserService;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() throws RuntimeException {
        log.info("Getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        log.info("Getting user with id {}", id);
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) throws ParseException {
        try {
            log.info("Creating user {}", user);
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            log.error("User with email {} already exists", user.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User with email " + user.getEmail() + " already exists");
        }
    }

}
