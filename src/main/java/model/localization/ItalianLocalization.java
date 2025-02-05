package model.localization;

import java.util.HashMap;
import java.util.Map;

public class ItalianLocalization implements Localization {

    private final Map<String, String> translations;

    public ItalianLocalization() {
        translations = new HashMap<>();

        // Login
        translations.put("login.title", "\n=== Pagina di Login ===");
        translations.put("login.username", "Nome Utente");
        translations.put("login.password", "Password");
        translations.put("login.submit", "Invia");
        translations.put("login.forgotPassword", "Password dimenticata?");
        translations.put("login.success", "Accesso effettuato con successo");
        translations.put("login.error", "Password o username non validi");
        translations.put("login.failed", "Accesso fallito");
        translations.put("error.empty.credentials", "Per favore, compila tutti i campi.");
        translations.put("error.invalid.credentials", "Credenziali non valide. Riprova.");
        translations.put("welcome.user","Benvenuto in Easify!");
        translations.put("error.invalid.option", "Opzione non valida. Riprova.");

        // Admin
        translations.put("admin.menu.title", "Menu Amministratore");
        translations.put("admin.menu.options", "1. Visualizza tutti gli utenti\n2. Aggiungi utente\n3. Rimuovi utente\n4. Vai ai progetti\n5. Indietro\n");
        translations.put("admin.users.title", "Lista Utenti");
        translations.put("admin.users.empty", "Nessun utente trovato");
        translations.put("admin.add.user", "Aggiungi Utente");
        translations.put("user.username", "Nome Utente");
        translations.put("user.password", "Password");
        translations.put("user.name", "Nome");
        translations.put("user.surname", "Cognome");
        translations.put("user.role", "Ruolo");
        translations.put("admin.remove.user", "Rimuovi Utente");
        translations.put("admin.remove.user.prompt", "Inserisci l'indice dell'utente da rimuovere:");
        translations.put("admin.confirm", "Conferma");
        translations.put("admin.add.project", "Aggiungi un progetto");
        translations.put("project.name", "Nome del progetto");
        translations.put("project.description", "Descrizione del progetto");
        translations.put("admin.remove.project", "Rimuovi Progetto");
        translations.put("admin.navigate.project", "Andando al Progetto");
        translations.put("admin.navigate.project.prompt", "Inserisci il nome del progetto verso cui vuoi navigare");
        translations.put("admin.menu.navigate.project", "Vai ai progetti");
        translations.put("admin.add.error", "Errore durante l'aggiunta dell'utente");
        translations.put("admin.remove.user.empty", "Nessun utente trovato");
        translations.put("admin.remove.user.list", "Seleziona un utente da rimuovere");
        translations.put("admin.remove.user.select", "Inserisci il numero dell'utente da rimuovere");
        translations.put("admin.input.invalid", "Input non valido. Inserisci un numero corretto.");
        translations.put("admin.remove.cancelled", "Rimozione utente annullata");
        translations.put("field.username.not.null", "Il campo username non può essere vuoto.");
        translations.put("field.password.not.null", "Il campo password non può essere vuoto.");
        translations.put("field.role.not.null", "Il campo ruolo non può essere vuoto.");
        translations.put("field.name.not.null", "Il campo nome non può essere vuoto.");
        translations.put("field.surname.not.null", "Il campo cognome non può essere vuoto.");

        // General
        translations.put("generic.back", "Indietro");
        translations.put("generic.select", "Seleziona");
        translations.put("generic.error", "Si è verificato un errore. Riprova.");
        translations.put("error.title", "Errore");
        translations.put("success.title", "Successo");
        translations.put("role.employee", "Dipendente");
        translations.put("role.projectManager", "Project Manager");
        translations.put("role.admin", "Amministratore");
        translations.put("role.unknownRole", "Ruolo sconosciuto");
        translations.put("interface.choose", "Scegli l'interfaccia:");
        translations.put("interface.graphic", "Interfaccia Grafica");
        translations.put("interface.cli", "Interfaccia a riga di comando");
        translations.put("interface.selected", "Interfaccia selezionata");
        translations.put("interface.prompt", "Inserisci la tua scelta");
        translations.put("interface.invalid", "Scelta non valida. Riprova.");
        translations.put("error.no.previous.view", "Nessuna vista precedente a cui tornare.");
        translations.put("view.close", "Chiudendo la pagina");
        translations.put("generic.action.success", "Operazione completata con successo\n");
        translations.put("generic.action.cancelled", "Azione annullata\n");
        translations.put("field", "Il campo: ");
        translations.put("field.not.null", " non può essere nullo o vuoto");
        translations.put("error.invalid.selection", "Selezione non valida, riprova");
        translations.put("user.not.found", "Utente non trovato");

        // Project
        translations.put("project.view.title", "Progetti");
        translations.put("project.add.prompt.name", "Inserisci il nome del progetto:");
        translations.put("project.add.prompt.description", "Inserisci la descrizione del progetto:");
        translations.put("project.add.success", "Progetto aggiunto con successo.");
        translations.put("project.delete.prompt.name", "Inserisci il nome del progetto da eliminare:");
        translations.put("project.delete.success", "Progetto eliminato con successo.");
        translations.put("error.load.projects", "Errore durante il caricamento dei progetti");
        translations.put("success.add.user.project", "Utente aggiunto al progetto con successo");
        translations.put("error.add.user.project", "Errore durante l'aggiunta dell'utente al progetto");
        translations.put("success.remove.user.project", "Utente rimosso dal progetto con successo");
        translations.put("error.remove.user.project", "Errore durante la rimozione dell'utente dal progetto");
        translations.put("error.navigate.project.details", "Errore durante la navigazione ai dettagli del progetto");
        translations.put("success.add.project", "Progetto aggiunto con successo");
        translations.put("error.add.project", "Errore durante l'aggiunta del progetto");
        translations.put("success.delete.project", "Progetto eliminato con successo");
        translations.put("error.delete.project", "Errore durante l'eliminazione del progetto");
        translations.put("project.list.title", "\n===Lista Progetti ===");
        translations.put("project.list.empty", "Nessun progetto trovato.");
        translations.put("project.menu.title", "\n=== Menu Progetto ===");
        translations.put("project.menu.options", "1. Visualizza progetti\n2. Aggiungi un dipendente al progetto\n3. Elimina un dipendente dal progetto\n4. Vai alle conversazioni\n5. Indietro");
        translations.put("project.menu.admin.options", "1. Visualizza progetti\n2. Agggiungi un dipendente al progetto\n3. Elimina un dipendente dal progetto\n4.Vai alle conversazioni\n5. Indietro\n6. Crea un nuovo progetto\n7. Elimina un progetto");
        translations.put("project.add.user.prompt.username", "Inserisci il nome utente da aggiungere:");
        translations.put("project.add.user.success", "Utente aggiunto al progetto con successo.");
        translations.put("project.add.user.error", "Impossibile aggiungere l'utente al progetto");
        translations.put("project.remove.user.prompt.username", "Inserisci il nome utente da rimuovere:");
        translations.put("project.remove.user.success", "Utente rimosso dal progetto con successo.");
        translations.put("project.remove.user.error", "Impossibile rimuovere l'utente dal progetto");
        translations.put("project.details.prompt", "Inserisci il nome del progetto per visualizzare i dettagli:");
        translations.put("project.details.error", "Impossibile accedere ai dettagli del progetto");
        translations.put("project.add.error", "Impossibile aggiungere il progetto");
        translations.put("project.delete.error", "Impossibile eliminare il progetto");
        translations.put("project.no.selected", "Nessun progetto selezionato");
        translations.put("project.button.add.user", "Aggiungi utente");
        translations.put("project.button.delete.user", "Elimina utente");
        translations.put("project.button.view.conversations", "Vai alle conversazioni");
        translations.put("project.remove.user.empty", "Nessun utente trovato in questo progetto.");
        translations.put("project.remove.user.title", "Rimuovi Utente dal Progetto");
        translations.put("project.add.user.title", "Aggiungi Utente al Progetto");
        translations.put("project.not.null", "Il nome o la descrizione del progetto non possono essere nulli");
        translations.put("project.select.prompt", "Seleziona l'indice di un progetto");
        translations.put("project.details.view", "\nProgetto");
        translations.put("project.add.user.error.already.in.project", "L'utente è già presente nel progetto");


        // Admin Project
        translations.put("admin.options", "Opzioni Admin:");
        translations.put("admin.option.add.project", "Aggiungi Progetto");
        translations.put("admin.option.delete.project", "Elimina Progetto");
        translations.put("admin.remove.project.success", "Progetto eliminato con successo");

        // Conversation
        translations.put("conversation.view.title", "Vista Conversazioni");
        translations.put("conversation.details.view", "Dettagli Conversazione");
        translations.put("conversation.no.selected", "Nessuna conversazione selezionata.");
        translations.put("conversation.button.send", "Invia Messaggio");
        translations.put("conversation.button.view.messages", "Visualizza Messaggi");
        translations.put("conversation.list.title", "\n=== Conversazioni ===");
        translations.put("conversation.no.conversations", "Nessuna conversazione disponibile.");
        translations.put("conversation.message.prompt", "Inserisci il tuo messaggio: ");
        translations.put("conversation.message.sent", "Messaggio inviato con successo!");
        translations.put("conversation.error.sending", "Invio del messaggio fallito.");
        translations.put("conversation.error.loading", "Errore nel caricamento delle conversazioni.");
        translations.put("conversation.no.permission", "Non hai il permesso di accedere a questa conversazione.");
        translations.put("conversation.message.input.reset", "Campo di input del messaggio cancellato.");
        translations.put("conversation.menu.title", "\n=== Menu Conversazioni ===");
        translations.put("conversation.menu.options", "1) Visualizza Messaggi\n2) Invia Messaggio\n3) Indietro");
        translations.put("conversation.list.empty", "Nessuna conversazione disponibile.");
        translations.put("conversation.messages", "\n=== Messaggi in questa conversazione ===");
        translations.put("conversation.messages.empty", "Nessun messaggio in questa conversazione.");
        translations.put("conversation.message.reset", "Input del messaggio cancellato");
        translations.put("conversation.selected", "Conversazione selezionata");
        translations.put("view.back", "Ritorno alla vista precedente...");
        translations.put("conversation.option.invalid", "Opzione selezionata non valida");
        translations.put("conversation.add.prompt.description", "Inserisci la descrizione della conversazione");
        translations.put("conversation.add.error.empty.description", "La descrizione della conversazione non può essere vuota");
        translations.put("conversation.add.success", "Conversazione aggiunta con successo");
        translations.put("conversation.add.error", "Errore nell'aggiunta della conversazione");
        translations.put("conversation.delete.prompt", "Seleziona la conversazione da eliminare");
        translations.put("conversation.delete.select", "Scegli un numero di conversazione");
        translations.put("conversation.delete.invalid", "Selezione non valida. Inserisci un numero corretto.");
        translations.put("conversation.delete.success", "Conversazione eliminata con successo");
        translations.put("conversation.delete.error", "Errore nell'eliminazione della conversazione");
        translations.put("conversation.delete.permission.denied", "Non hai i permessi per eliminare le conversazioni");
        translations.put("conversation.button.create", "Crea Conversazione");
        translations.put("conversation.button.delete", "Elimina Conversazione");
        translations.put("conversation.not.null", "La descrizione della conversazione non può essere nulla");
        translations.put("conversation.select.prompt", "Seleziona una conversazione");
        translations.put("conversation.select.index", "Inserisci l'indice della conversazione che vuoi selezionare");
        translations.put("conversation.add.user.prompt", "Seleziona un utente da aggiungere alla conversazione:");
        translations.put("conversation.add.user.success", "L'utente è stato aggiunto con successo alla conversazione.");
        translations.put("conversation.add.user.error", "Errore nell'aggiunta dell'utente alla conversazione.");
        translations.put("conversation.remove.user.prompt", "Seleziona un utente da rimuovere dalla conversazione:");
        translations.put("conversation.remove.user.success", "L'utente è stato rimosso con successo dalla conversazione.");
        translations.put("conversation.remove.user.error", "Errore nella rimozione dell'utente dalla conversazione.");
        translations.put("conversation.button.add.user", "Aggiungi Utente");
        translations.put("conversation.button.remove.user", "Rimuovi Utente");
        translations.put("conversation.select.user", "Aggiungi un utente alla conversazione:");
        translations.put("conversation.unselect.user", "Rimuovi un utente dalla conversazione:");
        translations.put("conversation.user.not.found", "Nessun utente trovato per la conversazione.");
        translations.put("conversation.error.add.user.generic", "Errore generico durante l'aggiunta di un utente.");
        translations.put("conversation.error.remove.user.generic", "Errore generico durante la rimozione di un utente.");
        translations.put("conversation.error.permission.denied", "Non hai i permessi per modificare questa conversazione.");
        translations.put("conversation.menu.pm.title", "\n=== Menu conversazioni PM ===");
        translations.put("conversation.menu.pm.options", "1) Visualizza Messaggi\n2) Invia Messaggio\n3) Indietro\n4)Aggiungi conversazione\n5)Elimina conversazione\n6)Aggiungi un utente a una conversazione\n7)Rimuovi un utente da una conversazione");
        translations.put("conversation.add.user.select", "Inserisci l'indice dell'utente che vuoi aggiungere");
    }

    @Override
    public String getText(String key) {
        return translations.getOrDefault(key, "Chiave non trovata: " + key);
    }
}
