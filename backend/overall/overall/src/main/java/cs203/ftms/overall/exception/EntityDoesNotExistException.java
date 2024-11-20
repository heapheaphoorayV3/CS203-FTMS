package cs203.ftms.overall.exception;

/**
 * Exception thrown when an entity is requested but does not exist in the database.
 */
public class EntityDoesNotExistException extends RuntimeException {

    /**
     * Constructs a new EntityDoesNotExistException with the specified detail message.
     *
     * @param message The detail message, providing information about the cause of the exception.
     */
    public EntityDoesNotExistException(String message) {
        super(message);
    }
    
}

