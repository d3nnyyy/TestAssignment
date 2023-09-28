package ua.dtsebulia.testassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.dtsebulia.testassignment.exception.UserAlreadyExistsException;
import ua.dtsebulia.testassignment.exception.UserNotFoundException;
import ua.dtsebulia.testassignment.model.User;
import ua.dtsebulia.testassignment.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
    }

    public User createUser(User user) {

        String email = user.getEmail();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }

        return userRepository.save(user);
    }
}
