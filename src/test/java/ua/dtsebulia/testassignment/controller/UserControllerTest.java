package ua.dtsebulia.testassignment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.dtsebulia.testassignment.exception.InvalidDateFormatException;
import ua.dtsebulia.testassignment.exception.InvalidDateRangeException;
import ua.dtsebulia.testassignment.exception.MinimumAgeException;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testGetUserById() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        Integer userId = 1;
        when(userService.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: " + userId, response.getBody());
    }

    @Test
    public void testGetUserByBirthdayRange() throws ParseException {

        String from = "2000-01-01";
        String to = "2005-01-01";

        List<User> users = new ArrayList<>();
        users.add(
                new User(
                        1,
                        "John",
                        "Doe",
                        "john.doe@gmail.com", new SimpleDateFormat("yyyy-MM-dd").parse("2002-01-01"),
                        "New York",
                        "+380680123456"
                )
        );
        users.add(
                new User(
                        2,
                        "Jane",
                        "Doe",
                        "jane.doe@gmail.com",
                        new SimpleDateFormat("yyyy-MM-dd").parse("1999-01-01"),
                        "New York",
                        "+380680123456"
                )
        );

        when(userService.getUserByBirthdayRange(from, to)).thenReturn(users);
        ResponseEntity<?> response = userController.getUserByBirthdayRange(from, to);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());

    }

    @Test
    public void testGetUserByBirthdayRangeInvalidDateFormat() {
        String invalidFrom = "2000-20-20";
        String validTo = "2000-01-01";

        doThrow(new InvalidDateFormatException("The format of the date must be yyyy-MM-dd")).when(userService).getUserByBirthdayRange(invalidFrom, validTo);

        ResponseEntity<?> response = userController.getUserByBirthdayRange(invalidFrom, validTo);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The format of the date must be yyyy-MM-dd", response.getBody());
    }

    @Test
    public void testGetUserByBirthdayRangeInvalidDateRange() {
        String from = "2005-01-01";
        String to = "2000-01-01";

        doThrow(new InvalidDateRangeException("'from' date must be before 'to' date")).when(userService).getUserByBirthdayRange(from, to);

        ResponseEntity<?> response = userController.getUserByBirthdayRange(from, to);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'from' date must be before 'to' date", response.getBody());
    }

    @Test
    public void testCreateUser() throws ParseException {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        newUser.setAddress("New York");
        newUser.setPhoneNumber("+1234567890");

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<?> response = userController.createUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }

    @Test
    public void testCreateUserValidationFailure() throws ParseException {
        User invalidUser = new User();
        invalidUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));

        when(userService.createUser(any(User.class))).thenThrow(new MinimumAgeException("User is not above minimum age"));

        ResponseEntity<?> response = userController.createUser(invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User is not above minimum age", response.getBody());
    }

    @Test
    public void testUpdateUser() throws ParseException {
        Integer userId = 1;
        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated.user@example.com");
        updatedUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1985-01-01"));
        updatedUser.setAddress("Updated Address");
        updatedUser.setPhoneNumber("+9876543210");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    public void testUpdateUserValidationFailure() throws ParseException {
        Integer userId = 1;
        User invalidUser = new User();
        invalidUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));

        when(userService.updateUser(eq(userId), any(User.class))).thenThrow(new MinimumAgeException("User is not above minimum age"));

        ResponseEntity<?> response = userController.updateUser(userId, invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User is not above minimum age", response.getBody());
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 1;

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User with id " + userId + " was deleted", response.getBody());
    }


    @Test
    public void testDeleteUserNotFound() {
        Integer userId = 1;

        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(userId);

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: " + userId, response.getBody());
    }


}
