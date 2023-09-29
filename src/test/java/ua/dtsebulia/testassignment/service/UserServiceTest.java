package ua.dtsebulia.testassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import ua.dtsebulia.testassignment.exception.MinimumAgeException;
import ua.dtsebulia.testassignment.exception.UserAlreadyExistsException;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    public void testGetUserById() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdNotFound() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
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

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertEquals(newUser, result);
    }


    @Test
    public void testCreateUserWithExistingEmail() throws ParseException {
        User existingUser = new User();
        existingUser.setEmail("john.doe@gmail.com");

        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@gmail.com");
        newUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(newUser));
    }


    @Test
    public void testCreateUserBelowMinimumAge() throws ParseException {
        User userBelowMinimumAge = new User();
        userBelowMinimumAge.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2006-01-01"));

        int minimumAge = 18;

        ReflectionTestUtils.setField(userService, "minimumAge", minimumAge);

        assertThrows(MinimumAgeException.class, () -> userService.createUser(userBelowMinimumAge));
    }

    @Test
    public void testUpdateUser() throws ParseException {
        Integer userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        existingUser.setAddress("New York");
        existingUser.setPhoneNumber("+1234567890");

        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated.user@example.com");
        updatedUser.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1985-01-01"));
        updatedUser.setAddress("Updated Address");
        updatedUser.setPhoneNumber("+9876543210");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertEquals(updatedUser, result);
    }

    @Test
    public void testUpdateUserBelowMinimumAge() throws ParseException {
        Integer userId = 1;
        User userBelowMinimumAge = new User();
        userBelowMinimumAge.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));

        int minimumAge = 18;

        ReflectionTestUtils.setField(userService, "minimumAge", minimumAge);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertThrows(MinimumAgeException.class, () -> userService.updateUser(userId, userBelowMinimumAge));
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    public void testDeleteUserNotFound() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }


}