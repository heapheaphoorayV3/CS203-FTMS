package cs203.ftms.overall.exception;

/**
 * Exception thrown when an operation is attempted on a tournament that has already started.
 */
public class TournamentAlreadyStartedException extends RuntimeException {

    /**
     * Constructs a new TournamentAlreadyStartedException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public TournamentAlreadyStartedException(String message) {
        super(message);
    }
}
