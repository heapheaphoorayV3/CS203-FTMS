package cs203.ftms.overall.exception;

/**
 * Exception thrown when a fencer's debut year is invalid.
 */
public class InvalidDebutYearException extends RuntimeException {

    /**
     * Constructs a new InvalidDebutYearException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public InvalidDebutYearException(String message) {
        super(message);
    }
}
