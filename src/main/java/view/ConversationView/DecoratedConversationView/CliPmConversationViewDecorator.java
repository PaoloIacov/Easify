package view.ConversationView.DecoratedConversationView;

import model.localization.LocalizationManager;
import view.ConversationView.ConversationView;

import java.util.List;
import java.util.Scanner;

public class CliPmConversationViewDecorator extends ConversationViewDecorator {
    private final LocalizationManager localizationManager;
    private final Scanner scanner = new Scanner(System.in);

    public CliPmConversationViewDecorator(ConversationView conversationView, LocalizationManager localizationManager) {
        super(conversationView);
        this.localizationManager = localizationManager;
    }

    @Override
    public void display() {
        displayPmMenu();
    }

    private void displayPmMenu() {
        System.out.println("\n=== " + localizationManager.getText("conversation.menu.pm.title") + " ===");
        System.out.println(localizationManager.getText("conversation.menu.pm.options"));
    }

    public String handleDeleteConversation(List<String> conversationDescriptions) {
        if (conversationDescriptions == null || conversationDescriptions.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.delete.no.conversations"));
            return null;
        }

        System.out.println(localizationManager.getText("conversation.delete.prompt"));
        for (int i = 0; i < conversationDescriptions.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + conversationDescriptions.get(i));
        }

        String selectedIndex = getInput("conversation.delete.select");

        try {
            int choice = Integer.parseInt(selectedIndex);
            if (choice > 0 && choice <= conversationDescriptions.size()) {
                String selectedConversation = conversationDescriptions.get(choice - 1);
                showSuccess("conversation.delete.success");
                return selectedConversation;
            }
        } catch (NumberFormatException e) {
            showError("conversation.delete.invalid");
        }

        return null;
    }

    public String showAddUserDialog(List<String> usernames) {
        System.out.println(localizationManager.getText("conversation.add.user.prompt"));
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + usernames.get(i));
        }
        System.out.print(localizationManager.getText("conversation.add.user.select") + ": ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= usernames.size()) {
                return usernames.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText("conversation.add.user.invalid"));
        }
        return null;
    }

    public String showRemoveUserDialog(List<String> usernames) {
        System.out.println(localizationManager.getText("conversation.remove.user.prompt"));
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + usernames.get(i));
        }
        System.out.print(localizationManager.getText("conversation.remove.user.select") + ": ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= usernames.size()) {
                return usernames.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText("conversation.remove.user.invalid"));
        }
        return null;
    }
}

