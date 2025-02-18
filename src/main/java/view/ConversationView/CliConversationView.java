package view.ConversationView;

import controller.ConversationController;
import model.bean.ConversationBean;
import model.bean.MessageBean;
import model.localization.LocalizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliConversationView implements ConversationView {

    private final LocalizationManager localizationManager;
    private final ConversationController conversationController;
    private final Scanner scanner;
    private List<ConversationBean> conversationList = new ArrayList<>();
    private ConversationBean selectedConversation;

    public CliConversationView(LocalizationManager localizationManager, ConversationController conversationController) {
        this.localizationManager = localizationManager;
        this.conversationController = conversationController;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void display() {
        while (true) {
            System.out.println("\n=== " + localizationManager.getText("conversation.menu.title") + " ===");
            System.out.println(localizationManager.getText("conversation.menu.options"));
            System.out.print(localizationManager.getText("interface.prompt"));
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1" -> displayConversations();
                case "2" -> selectConversation();
                case "3" -> sendMessage();
                case "6" -> {
                    close();
                    return;
                }
                default -> showError(localizationManager.getText("error.invalid.option"));
            }
        }
    }

    @Override
    public void displayConversations() {
        try {
            List<ConversationBean> conversations = conversationController.getConversationsForUser();
            if (conversations.isEmpty()) {
                System.out.println(localizationManager.getText("conversation.list.empty"));
                return;
            }

            this.conversationList = new ArrayList<>(conversations);

            System.out.println(localizationManager.getText("conversation.list.title"));
            printConversationList(conversations);
        } catch (Exception e) {
            showError(localizationManager.getText("conversation.error.loading"));
        }
    }

    private void selectConversation() {
        if (conversationList.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.list.empty"));
            return;
        }

        System.out.println(localizationManager.getText("conversation.select.prompt"));
        printConversationList(conversationList);

        System.out.print(localizationManager.getText("conversation.select.index") + ": ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > conversationList.size()) {
                System.out.println(localizationManager.getText("error.invalid.selection"));
                return;
            }
            selectedConversation = conversationList.get(choice - 1);
            System.out.println(localizationManager.getText("conversation.selected") + ": " + selectedConversation.getDescription());

            displayMessages(selectedConversation);

        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText("error.invalid.selection"));
        }
    }

    @Override
    public void displayMessages(ConversationBean selectedConversation) {
        if (selectedConversation == null) {
            System.out.println(localizationManager.getText("conversation.no.selected"));
            return;
        }

        try {
            List<MessageBean> messages = conversationController.getMessagesForConversation(selectedConversation.getConversationID());
            System.out.println(localizationManager.getText("conversation.messages"));
            if (messages.isEmpty()) {
                System.out.println(localizationManager.getText("conversation.messages.empty"));
            } else {
                for (MessageBean message : messages) {
                    System.out.println(message.getSenderUsername() + ": " + message.getContent());
                }
            }
        } catch (Exception e) {
            showError(localizationManager.getText("conversation.error.loading.messages"));
        }
    }

    @Override
    public void sendMessage() {
        if (selectedConversation == null) {
            showError(localizationManager.getText("conversation.no.selected"));
            return;
        }

        System.out.print(localizationManager.getText("conversation.message.prompt") + ": ");
        String message = scanner.nextLine().trim();

        if (message.isEmpty()) {
            showError(localizationManager.getText("conversation.error.empty.message"));
            return;
        }

        try {
            conversationController.sendMessage(selectedConversation.getConversationID(), message);
            showSuccess(localizationManager.getText("conversation.message.sent"));
            displayMessages(selectedConversation);
        } catch (Exception e) {
            showError(localizationManager.getText("conversation.error.sending"));
        }
    }

    private void printConversationList(List<ConversationBean> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + conversations.get(i).getDescription());
        }
    }

    @Override
    public ConversationBean getSelectedConversation() {
        return selectedConversation;
    }

    @Override
    public String getMessageInput() {
        System.out.print(localizationManager.getText("conversation.message.prompt") + ": ");
        String message = scanner.nextLine().trim();

        if (message.isEmpty()) {
            showError(localizationManager.getText("conversation.error.empty.message"));
            return null;
        }

        return message;
    }

    @Override
    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey) + ": ");
        return scanner.nextLine().trim();
    }

    @Override
    public void showSuccess(String message) {
        System.out.println("\n" + localizationManager.getText("success.title") + ": " + message);
    }

    @Override
    public void showError(String message) {
        System.err.println("\n" + localizationManager.getText("error.title") + ": " + message);
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("admin.close"));
    }

    @Override
    public void refresh() {
        // Not needed for CLI
    }

    @Override
    public void back() {
        conversationController.back();
    }

    @Override
    public boolean isGraphic() {
        return false;
    }
}
