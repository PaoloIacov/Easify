package controller;

import model.localization.LocalizationManager;
import view.View;

public class LocalizationController {

    private final LocalizationManager localizationManager;
    private final View view;

    public LocalizationController(LocalizationManager localizationManager, View view) {
        this.localizationManager = localizationManager;
        this.view = view;
    }

    public void changeLanguage(String languageCode) {
        localizationManager.setLanguage(languageCode);
        view.refresh(); // La View si aggiorna per riflettere la nuova lingua
    }

    public String getLocalizedText(String key) {
        return localizationManager.getText(key);
    }
}
