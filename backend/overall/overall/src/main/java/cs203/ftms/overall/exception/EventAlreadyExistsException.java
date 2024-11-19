package cs203.ftms.overall.exception;

/**
 * Exception thrown when an attempt is made to create an event that already exists.
 */
public class EventAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new EventAlreadyExistsException with a default message indicating
     * that the event already exists.
     * 
     * @param message The detail message, providing information about the cause of the exception.
     */
    public EventAlreadyExistsException(String message) {
        super(message);
    }
}
