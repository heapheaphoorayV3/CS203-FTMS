package cs203.ftms.user.exception;

public class SignUpDateNotOverException extends RuntimeException {
    public SignUpDateNotOverException() {
        super("Sign up date is not over!");
    }
}
