package controller.exceptions.LoginExceptions;

public class EmptyCredentialsException extends LoginException {
    public EmptyCredentialsException() {
        super("error.empty.credentials");
    }
}
