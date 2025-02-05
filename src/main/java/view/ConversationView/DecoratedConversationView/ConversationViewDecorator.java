package view.ConversationView.DecoratedConversationView;

import model.bean.ConversationBean;
import model.bean.MessageBean;
import view.ConversationView.ConversationView;

import java.util.List;

public abstract class ConversationViewDecorator implements ConversationView {
    protected final ConversationView decoratedConversationView;

    protected ConversationViewDecorator(ConversationView conversationView) {
        this.decoratedConversationView = conversationView;
    }

    @Override
    public void displayConversations(List<ConversationBean> conversations) {
        decoratedConversationView.displayConversations(conversations);
    }

    @Override
    public void displayMessages(List<MessageBean> messages) {
        decoratedConversationView.displayMessages(messages);
    }

    @Override
    public void sendMessage(String message) {
        decoratedConversationView.sendMessage(message);
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
    public void setActionHandler(controller.ActionHandler handler) {
        decoratedConversationView.setActionHandler(handler);
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
    public void resetMessageInput() {
        decoratedConversationView.resetMessageInput();
    }

    @Override
    public String getMessageInput() {
        return decoratedConversationView.getMessageInput();
    }


}
