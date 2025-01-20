package model.bean;

import java.io.Serializable;

public class ConversationBean implements Serializable {

    private long conversationID;
    private String description;
    private String projectName;

    public ConversationBean() {
    }

    public ConversationBean(long id, String description, String projectName) {
        this.conversationID = id;
        this.description = description;
        this.projectName = projectName;
    }

    public long getConversationID() {
        return conversationID;
    }

    public void setConversationID(long conversationID) {
        this.conversationID = conversationID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
