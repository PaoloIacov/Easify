package model.converter;

import model.bean.MessageBean;
import model.domain.Message;


public class MessageConverter {

    private MessageConverter() {}

    public static MessageBean toBean(Message message) {
        return new MessageBean(
                message.getConversationID(),
                message.getSenderUsername(),
                message.getContent(),
                message.getDatetime()
        );
    }

    public static Message toDomain(MessageBean messageBean) {
        return new Message(
                messageBean.getConversationID(),
                messageBean.getSenderUsername(),
                messageBean.getContent(),
                messageBean.getDatetime()
        );
    }
}
