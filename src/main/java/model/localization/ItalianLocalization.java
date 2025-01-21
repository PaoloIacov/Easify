package model.localization;

import java.util.HashMap;
import java.util.Map;

public class ItalianLocalization implements Localization {

    private final Map<String, String> translations;

    public ItalianLocalization() {
        translations = new HashMap<>();

        //LoginPage
        translations.put("login.title", "Pagina di Login");
        translations.put("login.username", "Nome Utente");
        translations.put("login.password", "Password");
        translations.put("login.submit", "Invia");
        translations.put("login.forgotPassword", "Password dimenticata?");
        translations.put("error.generic", "Si è verificato un errore. Riprova.");
        translations.put("login.success", "Accesso effettuato con successo");
        translations.put("login.error", "Password o username non validi");
        translations.put("login.back" , "Indietro");
        translations.put("error.title", "Errore");
        translations.put("success.title", "Successo");
    }

    @Override
    public String getText(String key) {
        return translations.getOrDefault(key, "Chiave non trovata: " + key);
    }
}
