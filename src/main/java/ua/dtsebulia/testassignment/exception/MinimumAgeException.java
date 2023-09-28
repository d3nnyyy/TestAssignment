package ua.dtsebulia.testassignment.exception;

/**
 * Exception thrown when the age is less than minimum allowed.
 */
public class MinimumAgeException extends RuntimeException {
    public MinimumAgeException(String message) {
        super(message);
    }
}
