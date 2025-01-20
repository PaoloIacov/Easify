package model.domain;

public class Conversation {

    private long conversationID;
    private String description;
    private String projectName;

    public Conversation() {
    }

    public Conversation(long id, String description, String projectName) {
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
