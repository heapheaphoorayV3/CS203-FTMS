package cs203.ftms.overall.exception;

/**
 * Exception thrown when an attempt is made to register a fencer for an event
 * they are already registered for.
 */
public class FencerAlreadyRegisteredForEventException extends RuntimeException {

    /**
     * Constructs a new FencerAlreadyRegisteredForEventException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public FencerAlreadyRegisteredForEventException(String message) {
        super(message);
    }
}
