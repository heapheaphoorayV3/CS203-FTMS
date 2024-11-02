package cs203.ftms.user.exception;

public class FencerProfileIncompleteException extends RuntimeException {
    public FencerProfileIncompleteException() {
        super("Fencer profile incomplete, unable to perform operation!");
    }
}
