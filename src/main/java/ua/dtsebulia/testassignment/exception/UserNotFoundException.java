package ua.dtsebulia.testassignment.exception;

/**
 * Exception thrown when the user is not found.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
