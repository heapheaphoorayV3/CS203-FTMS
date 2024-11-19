package cs203.ftms.overall.exception;

/**
 * Exception thrown when there is a mismatch in the fencer profile data.
 * This runtime exception is used to indicate that the expected fencer profile
 * does not match the provided or existing data.
 */
public class FencerProfileMismatchException extends RuntimeException {

    /**
     * Constructs a new FencerProfileMismatchException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public FencerProfileMismatchException(String message) {
        super(message);
    }
}

