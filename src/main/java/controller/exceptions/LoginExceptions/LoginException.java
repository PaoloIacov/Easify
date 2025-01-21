package controller.exceptions.LoginExceptions;

public abstract class LoginException extends Exception {
    private final String messageKey;

    protected LoginException(String messageKey) {
        super();
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
