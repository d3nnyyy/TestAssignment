package ua.dtsebulia.testassignment.exception;

/**
 * Exception thrown when the date format is invalid.
 */
public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
