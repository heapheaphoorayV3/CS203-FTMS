package cs203.ftms.user.exception;

public class EventCannotEndException extends RuntimeException {
    public EventCannotEndException(String message) {
        super(message);
    }
}