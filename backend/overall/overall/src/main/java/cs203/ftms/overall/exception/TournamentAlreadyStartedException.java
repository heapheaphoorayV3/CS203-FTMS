package cs203.ftms.overall.exception;

public class TournamentAlreadyStartedException extends RuntimeException {
    public TournamentAlreadyStartedException(String message) {
        super(message);
    }
    
}
