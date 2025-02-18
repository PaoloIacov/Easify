package view.ConversationView.DecoratedConversationView;

import model.bean.ConversationBean;
import view.ConversationView.ConversationView;
import java.sql.SQLException;

public abstract class ConversationViewDecorator implements ConversationView {
    protected final ConversationView decoratedConversationView;

    protected ConversationViewDecorator(ConversationView conversationView) {
        this.decoratedConversationView = conversationView;
    }

    @Override
    public void displayConversations() throws SQLException {
        decoratedConversationView.displayConversations();
    }

    @Override
    public void displayMessages(ConversationBean selectedConversation) {
        decoratedConversationView.displayMessages(selectedConversation);
    }

    @Override
    public void sendMessage() {
        decoratedConversationView.sendMessage();
    }

    @Override
    public ConversationBean getSelectedConversation() {
        return decoratedConversationView.getSelectedConversation();
    }

    @Override
    public void showSuccess(String message) {
        decoratedConversationView.showSuccess(message);
    }

    @Override
    public void showError(String message) {
        decoratedConversationView.showError(message);
    }

    @Override
    public void display() {
        decoratedConversationView.display();
    }

    @Override
    public void close() {
        decoratedConversationView.close();
    }

    @Override
    public void refresh() {
        decoratedConversationView.refresh();
    }

    @Override
    public boolean isGraphic() {
        return decoratedConversationView.isGraphic();
    }

    @Override
    public String getInput(String promptKey) {
        return decoratedConversationView.getInput(promptKey);
    }

    @Override
    public void back() {
        decoratedConversationView.back();
    }

    @Override
    public String getMessageInput() {
        return decoratedConversationView.getMessageInput();
    }

}
