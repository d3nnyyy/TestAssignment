package ua.dtsebulia.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.dtsebulia.testassignment.exception.MinimumAgeException;
import ua.dtsebulia.testassignment.exception.UserAlreadyExistsException;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.repository.UserRepository;

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
}
