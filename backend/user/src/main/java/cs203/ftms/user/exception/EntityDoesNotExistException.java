package cs203.ftms.user.exception;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(String msg) {
        super(msg);
    }
    
}
