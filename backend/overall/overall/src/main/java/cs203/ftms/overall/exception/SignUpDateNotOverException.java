package cs203.ftms.overall.exception;

/**
 * Exception thrown when an operation requiring the sign-up period to be over is attempted,
 * but the sign-up date has not yet passed.
 */
public class SignUpDateNotOverException extends RuntimeException {

    /**
     * Constructs a new SignUpDateNotOverException with a default message indicating
     * that the sign-up date is not yet over.
     * 
     * @param message The detail message, providing information about the cause of the exception.
     */
    public SignUpDateNotOverException(String message) {
        super(message);
    }
}
