package view.ConversationView;

import model.bean.ConversationBean;
import view.View;

import java.sql.SQLException;

public interface ConversationView extends View {
    void displayConversations() throws SQLException;

    void displayMessages(ConversationBean selectedConversation);

    void sendMessage();

    ConversationBean getSelectedConversation();

    void showSuccess(String message);

    String getMessageInput();
}

