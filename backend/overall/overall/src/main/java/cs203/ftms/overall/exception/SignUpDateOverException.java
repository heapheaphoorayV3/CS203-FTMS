package cs203.ftms.overall.exception;

/**
 * Exception thrown when an operation is attempted after the sign-up date has passed.
 */
public class SignUpDateOverException extends RuntimeException {

    /**
     * Constructs a new SignUpDateOverException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public SignUpDateOverException(String message) {
        super(message);
    }
}
