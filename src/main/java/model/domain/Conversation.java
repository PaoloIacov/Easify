package model.domain;

public class Conversation {

    private Long conversationID;
    private String description;
    private String projectName;


    public Conversation(Long id, String description, String projectName) {
        this.conversationID = id;
        this.description = description;
        this.projectName = projectName;
    }

    public Long getConversationID() {
        return conversationID;
    }

    public void setConversationID(Long conversationID) {
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
