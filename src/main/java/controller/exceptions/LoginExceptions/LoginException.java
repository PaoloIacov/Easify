package controller.exceptions.LoginExceptions;

import java.io.IOException;

public abstract class LoginException extends Exception {
    private final String messageKey;

    protected LoginException(String messageKey) {
        super();
        this.messageKey = messageKey;
    }

    protected LoginException(String text, IOException e) {
        super(text, e);
        this.messageKey = text;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
