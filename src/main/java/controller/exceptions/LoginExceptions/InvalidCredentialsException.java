package controller.exceptions.LoginExceptions;

public class InvalidCredentialsException extends LoginException {
    public InvalidCredentialsException() {
        super("error.invalid.credentials");
    }
}
