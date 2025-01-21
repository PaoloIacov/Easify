package model.localization;

import java.util.HashMap;
import java.util.Map;

public class EnglishLocalization implements Localization {

    private final Map<String, String> translations;

    public EnglishLocalization() {
        translations = new HashMap<>();

        //LoginPage
        translations.put("login.title", "Login Page");
        translations.put("login.username", "Username");
        translations.put("login.password", "Password");
        translations.put("login.submit", "Submit");
        translations.put("login.forgotPassword", "Forgot Password?");
        translations.put("error.generic", "An error occurred. Please try again.");
        translations.put("login.success", "Login successful");
        translations.put("login.error", "Invalid username or password.");
        translations.put("login.back" , "Back");
        translations.put("error.title", "Error");
        translations.put("success.title", "Success");
    }

    @Override
    public String getText(String key) {
        return translations.getOrDefault(key, "Key not found: " + key);
    }
}

