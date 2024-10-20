package cs203.ftms.overall.exception;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException() {
        super("Event already exists!");
    }
}