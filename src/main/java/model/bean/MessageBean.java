package model.bean;

import java.io.Serializable;

public class MessageBean implements Serializable {

    private long messageID;
    private String senderUsername;
    private String content;
    private String formattedDate;

    public MessageBean() {}

    public MessageBean(long id, String senderUsername, String content, String formattedDate) {
        this.messageID = id;
        this.senderUsername = senderUsername;
        this.content = content;
        this.formattedDate = formattedDate;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
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

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}

