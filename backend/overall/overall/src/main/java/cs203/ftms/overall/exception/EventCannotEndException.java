package cs203.ftms.overall.exception;

/**
 * Exception thrown when an attempt is made to end an event that cannot be ended,
 * typically due to certain conditions not being met.
 */
public class EventCannotEndException extends RuntimeException {

    /**
     * Constructs a new EventCannotEndException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public EventCannotEndException(String message) {
        super(message);
    }
}
