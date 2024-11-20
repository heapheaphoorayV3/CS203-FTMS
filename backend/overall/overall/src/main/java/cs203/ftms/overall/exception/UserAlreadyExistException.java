package cs203.ftms.overall.exception;

/**
 * Exception thrown when an attempt is made to create a user that already exists.
 */
public class UserAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
