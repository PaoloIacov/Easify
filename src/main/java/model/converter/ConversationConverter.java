package model.converter;

import model.bean.ConversationBean;
import model.domain.Conversation;

public class ConversationConverter {

    private ConversationConverter() {
    }

    public static ConversationBean toBean(Conversation conversation) {
        return new ConversationBean(
                conversation.getConversationID(),
                conversation.getDescription(),
                conversation.getProjectName()
        );
    }

    public static Conversation toEntity(ConversationBean conversationBean) {
        return new Conversation(
                conversationBean.getConversationID(),
                conversationBean.getDescription(),
                conversationBean.getProjectName()
        );
    }
}
