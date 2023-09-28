package ua.dtsebulia.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.dtsebulia.testassignment.exception.*;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.repository.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${user.minimumAge}")
    private String minimumAge;

    /**
     * Get a list of all users.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The requested user.
     * @throws UserNotFoundException If no user with the specified ID exists.
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
    }

    /**
     * Create a new user.
     *
     * @param user The user object to be created.
     * @return The newly created user.
     * @throws InvalidDateFormatException If the date format is invalid.
     * @throws MinimumAgeException       If the user is not above the minimum age.
     * @throws UserAlreadyExistsException If a user with the same email already exists.
     */
    public User createUser(User user) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(user.getDateOfBirth().toString());
        } catch (ParseException e) {
            throw new InvalidDateFormatException("The format of the date must be yyyy-MM-dd");
        }

        if (!isUserAboveMinimumAge(user)) {
            throw new MinimumAgeException("User is not above minimum age");
        }

        String email = user.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }

        return userRepository.save(user);
    }

    // Helper method to check if the user is above the minimum age.

    /**
     * Check if a user is above the minimum age.
     *
     * @param user The user to check.
     * @return True if the user is above the minimum age, false otherwise.
     */
    private boolean isUserAboveMinimumAge(User user) {

        Calendar minBirthDate = Calendar.getInstance();
        minBirthDate.add(Calendar.YEAR, -Integer.parseInt(minimumAge));

        Calendar dob = Calendar.getInstance();
        dob.setTime(user.getDateOfBirth());

        return dob.before(minBirthDate) || dob.equals(minBirthDate);
    }

    /**
     * Delete a user by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @throws UserNotFoundException If the user is not found.
     */
    public void deleteUser(Integer id) {

        userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );

        userRepository.deleteById(id);
    }

    /**
     * Update an existing user.
     *
     * @param id   The ID of the user to be updated.
     * @param user The updated user object.
     * @return The updated user.
     * @throws UserNotFoundException   If the user is not found.
     * @throws MinimumAgeException     If the user is not above the minimum age.
     * @throws UserAlreadyExistsException If a user with the same email already exists.
     */
    public User updateUser(Integer id, User user) {

        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );

        if (!isUserAboveMinimumAge(user)) {
            throw new MinimumAgeException("User is not above minimum age");
        }

        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent() && !email.equals(existingUser.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }
        updateUserWithNullChecks(user, existingUser);
        return userRepository.save(existingUser);
    }

    // Helper method to update user fields with null checks.

    /**
     * Update user fields with null checks.
     *
     * @param user          The updated user object.
     * @param existingUser  The existing user object to update.
     */
    private static void updateUserWithNullChecks(User user, User existingUser) {
        if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
        if (user.getDateOfBirth() != null) existingUser.setDateOfBirth(user.getDateOfBirth());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
    }

    /**
     * Get users with birthdays within a specified date range.
     *
     * @param from Start date of the range.
     * @param to   End date of the range.
     * @return List of users within the date range.
     * @throws InvalidDateFormatException If the date format is invalid.
     * @throws InvalidDateRangeException  If the date range is invalid.
     */
    public List<User> getUserByBirthdayRange(String from, String to) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        Date fromDate;
        Date toDate;

        try {
            fromDate = dateFormat.parse(from);
            toDate = dateFormat.parse(to);
        } catch (ParseException e) {
            throw new InvalidDateFormatException("Invalid date format");
        }

        if (fromDate.after(toDate)) {
            throw new InvalidDateRangeException("'from' date must be before 'to' date");
        }

        return userRepository.findUsersByBirthdayRange(fromDate, toDate);
    }
}
