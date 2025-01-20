package model.domain;

import java.time.LocalDateTime;

public class Message {

    private long messageID;
    private long conversationID;
    private String senderUsername;
    private String content;
    private LocalDateTime datetime;

    public Message() {}

    public Message(long id, long conversationID, String senderUsername, String content, LocalDateTime datetime) {
        this.messageID = id;
        this.conversationID = conversationID;
        this.senderUsername = senderUsername;
        this.content = content;
        this.datetime = datetime;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getConversationID() {
        return conversationID;
    }

    public void setConversationID(long conversationID) {
        this.conversationID = conversationID;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
