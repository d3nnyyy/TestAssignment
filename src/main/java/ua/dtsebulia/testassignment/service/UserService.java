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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${user.minimumAge}")
    private String minimumAge;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
    }

    public User createUser(User user) {

        if (!isUserAboveMinimumAge(user)) {
            throw new MinimumAgeException("User is not above minimum age");
        }

        String email = user.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }

        return userRepository.save(user);
    }

    private boolean isUserAboveMinimumAge(User user) {

        Calendar minBirthDate = Calendar.getInstance();
        minBirthDate.add(Calendar.YEAR, -Integer.parseInt(minimumAge));

        Calendar dob = Calendar.getInstance();
        dob.setTime(user.getDateOfBirth());

        return dob.before(minBirthDate) || dob.equals(minBirthDate);
    }

    public void deleteUser(Integer id) {

        userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );

        userRepository.deleteById(id);
    }

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

    private static void updateUserWithNullChecks(User user, User existingUser) {
        if (user.getFirstName() != null) existingUser.setFirstName(user.getFirstName());
        if (user.getLastName() != null) existingUser.setLastName(user.getLastName());
        if (user.getDateOfBirth() != null) existingUser.setDateOfBirth(user.getDateOfBirth());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getPhoneNumber() != null) existingUser.setPhoneNumber(user.getPhoneNumber());
    }

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
