package cs203.ftms.overall.exception;

/**
 * Exception thrown when poule matches have not been completed.
 * This runtime exception is used to indicate that further operations 
 * cannot proceed until all poule matches are finished.
 */
public class PouleMatchesNotDoneException extends RuntimeException {

    /**
     * Constructs a new PouleMatchesNotDoneException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public PouleMatchesNotDoneException(String message) {
        super(message);
    }
}

