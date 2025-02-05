package view.ConversationView;

import controller.ActionHandler;
import model.bean.ConversationBean;
import model.bean.MessageBean;
import model.localization.LocalizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliConversationView implements ConversationView {

    private final LocalizationManager localizationManager;
    private final Scanner scanner;
    private List<ConversationBean> conversationList = new ArrayList<>();

    public CliConversationView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void display() {
        System.out.println(localizationManager.getText("conversation.menu.title"));
        System.out.println(localizationManager.getText("conversation.menu.options"));
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("view.close"));
    }

    @Override
    public void refresh() {
        System.out.println(localizationManager.getText("view.refresh"));
    }

    @Override
    public void back() {
        System.out.println(localizationManager.getText("view.back"));
    }

    @Override
    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey) + ": ");
        return scanner.nextLine();
    }

    @Override
    public boolean isGraphic() {
        return false;
    }

    @Override
    public void displayConversations(List<ConversationBean> conversations) {
        if (conversations == null || conversations.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.list.empty"));
            return;
        }

        this.conversationList = new ArrayList<>(conversations); // ✅ Aggiorna la lista locale

        System.out.println(localizationManager.getText("conversation.list.title"));
        printConversationList(conversations);
    }

    @Override
    public void displayMessages(List<MessageBean> messages) {
        System.out.println(localizationManager.getText("conversation.messages"));
        if (messages.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.messages.empty"));
        } else {
            for (MessageBean message : messages) {
                System.out.println(message.getSenderUsername() + ": " + message.getContent());
            }
        }
    }


    @Override
    public void sendMessage(String message) {
        System.out.println(localizationManager.getText("conversation.message.sent") + ": " + message);
    }

    @Override
    public ConversationBean getSelectedConversation() {
        if (conversationList == null || conversationList.isEmpty()) {
            System.out.println(localizationManager.getText("conversation.list.empty"));
            return null;
        }

        System.out.println(localizationManager.getText("conversation.select.prompt"));
        printConversationList(conversationList);

        System.out.print(localizationManager.getText("conversation.select.index") + ": ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > conversationList.size()) {
                System.out.println(localizationManager.getText("error.invalid.selection"));
                return null;
            }
            return conversationList.get(choice - 1); // ✅ Ritorna la conversazione selezionata
        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText("error.invalid.selection"));
            return null;
        }
    }

    @Override
    public String getMessageInput() {
        System.out.print(localizationManager.getText("conversation.message.prompt") + ": ");
        return scanner.nextLine();
    }

    @Override
    public void resetMessageInput() {
        // Do nothing
    }

    @Override
    public void showSuccess(String message) {
        System.out.println(localizationManager.getText("success.title") + ": " + message);
    }

    @Override
    public void showError(String message) {
        System.err.println(localizationManager.getText("error.title") + ": " + message);
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        throw new UnsupportedOperationException("CLI view does not support action handlers");
    }

    private void printConversationList(List<ConversationBean> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + conversations.get(i).getDescription());
        }
    }

}
