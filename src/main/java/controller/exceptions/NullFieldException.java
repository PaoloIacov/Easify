package controller.exceptions;

import model.localization.LocalizationManager;

public class NullFieldException extends Exception {
    private final String messageKey;
    private final transient LocalizationManager localizationManager;

    public NullFieldException(String messageKey, LocalizationManager localizationManager) {
        super(localizationManager.getText(messageKey));
        this.messageKey = messageKey;
        this.localizationManager = localizationManager;
    }

    public String getMessageKey() {
        return localizationManager.getText(messageKey);
    }


}
