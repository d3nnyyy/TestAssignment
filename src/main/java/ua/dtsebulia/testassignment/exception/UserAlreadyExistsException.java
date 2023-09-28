package ua.dtsebulia.testassignment.exception;

/**
 * Exception thrown when the user already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
