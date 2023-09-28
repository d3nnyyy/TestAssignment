package ua.dtsebulia.testassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.dtsebulia.testassignment.exception.*;
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

    @GetMapping("/birthdays")
    public ResponseEntity<?> getUserByBirthdayRange(@RequestParam String from, @RequestParam String to) {
        log.info("Getting users with birthdays between {} and {}", from, to);
        try {
            return ResponseEntity.ok(userService.getUserByBirthdayRange(from, to));
        } catch (InvalidDateRangeException ex) {
            log.error("'from' date must be before 'to' date");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("'from' date must be before 'to' date");
        } catch (InvalidDateFormatException ex) {
            log.error("The format of the date must be yyyy-MM-dd");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("The format of the date must be yyyy-MM-dd");
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) throws ParseException {
        try {
            log.info("Creating user {}", user);
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (MinimumAgeException ex){
            log.error("User is not above minimum age");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User is not above minimum age");
        }
        catch (UserAlreadyExistsException ex) {
            log.error("User with email {} already exists", user.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User with email " + user.getEmail() + " already exists");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user) {
        log.info("Updating user with id {}", id);
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        } catch (MinimumAgeException ex){
            log.error("User is not above minimum age");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User is not above minimum age");
        } catch (UserAlreadyExistsException ex) {
            log.error("User with email {} already exists", user.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User with email " + user.getEmail() + " already exists");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        log.info("Deleting user with id {}", id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User with id " + id + " was deleted");
        } catch (UserNotFoundException ex) {
            log.error("User not found with id: {}", id);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        }
    }

}
