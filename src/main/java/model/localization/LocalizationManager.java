package model.localization;

import java.util.Locale;
import java.util.Set;

public class LocalizationManager {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("it", "en");
    private Localization currentLocalization;

    public LocalizationManager() {

        String defaultLanguage = Locale.getDefault().getLanguage();
        if (!SUPPORTED_LANGUAGES.contains(defaultLanguage)) {
            defaultLanguage = "en"; //Fallback to English
        }
        setLanguage(defaultLanguage);
    }

    public void setLanguage(String languageCode) {
        switch (languageCode.toLowerCase()) {
            case "it":
                currentLocalization = new ItalianLocalization();
                break;
            case "en":
            default:
                currentLocalization = new EnglishLocalization();
                break;
        }
    }

    public String getText(String key) {
        return currentLocalization.getText(key);
    }
}
