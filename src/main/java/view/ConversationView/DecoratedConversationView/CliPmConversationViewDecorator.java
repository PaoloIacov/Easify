package view.ConversationView.DecoratedConversationView;

import model.localization.LocalizationManager;
import view.ConversationView.ConversationView;
import view.GeneralUtils;

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
        System.out.println(localizationManager.getText("conversation.menu.pm.title"));
        System.out.println(localizationManager.getText("conversation.menu.pm.options"));
    }

    public String handleDeleteConversation(List<String> conversationDescriptions) {
        if (conversationDescriptions == null || conversationDescriptions.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.delete.no.conversations"));
            return null;
        }

        System.out.println(localizationManager.getText("conversation.delete.prompt"));
        GeneralUtils.printList(conversationDescriptions, null);

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
        GeneralUtils.printList(usernames, null);
        System.out.print(localizationManager.getText("conversation.add.user.select") + ": ");

        return GeneralUtils.selectUsername(scanner, usernames, localizationManager.getText("conversation.add.user.invalid"));
    }

    public String showRemoveUserDialog(List<String> usernames) {
        System.out.println(localizationManager.getText("conversation.remove.user.prompt"));
        GeneralUtils.printList(usernames, null);
        System.out.print(localizationManager.getText("conversation.remove.user.select") + ": ");

        return GeneralUtils.selectUsername(scanner, usernames, localizationManager.getText("conversation.remove.user.invalid"));
    }
}

