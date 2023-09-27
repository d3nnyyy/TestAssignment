package ua.dtsebulia.testassignment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.service.UserService;

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

}
