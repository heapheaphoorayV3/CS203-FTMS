package cs203.ftms.overall.exception;

/**
 * Exception thrown when an operation cannot be performed due to an incomplete
 * fencer profile.
 */
public class FencerProfileIncompleteException extends RuntimeException {

    /**
     * Constructs a new FencerProfileIncompleteException with a default message indicating
     * that the fencer profile is incomplete.
     * 
     * @param message The detail message, providing information about the cause of the exception.
     */
    public FencerProfileIncompleteException(String message) {
        super(message);
    }
}
