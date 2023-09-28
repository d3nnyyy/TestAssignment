package ua.dtsebulia.testassignment.exception;

/**
 * Exception thrown when the date range is invalid.
 */
public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
