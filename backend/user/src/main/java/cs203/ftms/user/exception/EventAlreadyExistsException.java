package cs203.ftms.user.exception;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException() {
        super("Event already exists!");
    }
}