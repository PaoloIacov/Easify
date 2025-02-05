package view.ConversationView;

import controller.ActionHandler;
import model.bean.ConversationBean;
import model.bean.MessageBean;
import view.View;

import java.util.List;

public interface ConversationView extends View {
    void displayConversations(List<ConversationBean> conversations);
    void displayMessages(List<MessageBean> messages);
    void sendMessage(String message);
    ConversationBean getSelectedConversation();
    void showSuccess(String message);
    String getMessageInput();
    void resetMessageInput();
    void setActionHandler(ActionHandler handler);
}

