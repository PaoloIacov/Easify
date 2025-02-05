package model.domain;

import java.time.LocalDateTime;

public class Message {
    private Long conversationID;
    private String senderUsername;
    private String content;
    private LocalDateTime datetime;

    public Message(Long conversationID, String senderUsername, String content, LocalDateTime datetime) {
        this.conversationID = conversationID;
        this.senderUsername = senderUsername;
        this.content = content;
        this.datetime = datetime;
    }

    public Long getConversationID() {
        return conversationID;
    }

    public void setConversationID(Long conversationID) {
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
