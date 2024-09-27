package cs203.ftms.overall.exception;

public class FencerProfileIncompleteException extends RuntimeException {
    public FencerProfileIncompleteException() {
        super("Fencer profile incomplete, unable to perform operation!");
    }
}
