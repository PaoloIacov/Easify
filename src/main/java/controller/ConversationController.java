package controller;

import controller.exceptions.NullFieldException;
import model.bean.ConversationBean;
import model.bean.CredentialsBean;
import model.bean.MessageBean;
import model.bean.UserBean;
import model.converter.ConversationConverter;
import model.converter.MessageConverter;
import model.converter.UserConverter;
import model.dao.ConversationDAO;
import model.dao.MessageDAO;
import model.domain.Conversation;
import model.domain.Message;
import model.localization.LocalizationManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ConversationController {

    private final ConversationDAO conversationDAO;
    private final MessageDAO messageDAO;
    private final LocalizationManager localizationManager;
    private final CredentialsBean loggedInUser;
    private final ApplicationController applicationController;
    private String currentProjectName;

    public ConversationController(ConversationDAO conversationDAO, MessageDAO messageDAO, LocalizationManager localizationManager, CredentialsBean loggedInUser, ApplicationController applicationController, String currentProjectName) {
        this.conversationDAO = conversationDAO;
        this.messageDAO = messageDAO;
        this.localizationManager = localizationManager;
        this.loggedInUser = loggedInUser;
        this.applicationController = applicationController;
        this.currentProjectName = currentProjectName;
    }

    public void setProject(String projectName) {
        this.currentProjectName = projectName;
    }

    public List<ConversationBean> getConversationsForUser() throws SQLException {
        if (loggedInUser.getRole() == 2 || loggedInUser.getRole() == 1) {
            return conversationDAO.getConversationsForUser(loggedInUser.getUsername(), loggedInUser.getRole())
                    .stream()
                    .map(ConversationConverter::toBean)
                    .toList();
        } else {
            System.out.println(currentProjectName);
            System.out.println(conversationDAO.getConversationsForProject(currentProjectName));
            return conversationDAO.getConversationsForProject(currentProjectName)
                    .stream()
                    .map(ConversationConverter::toBean)
                    .toList();
        }
    }

    public List<MessageBean> getMessagesForConversation(Long conversationId) {
        return messageDAO.getMessagesByConversationID(conversationId)
                .stream()
                .map(MessageConverter::toBean)
                .toList();
    }

    public void sendMessage(Long conversationId, String messageContent) throws NullFieldException {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            throw new NullFieldException("conversation.error.empty.message", localizationManager);
        }

        MessageBean newMessageBean = new MessageBean(
                conversationId,
                loggedInUser.getUsername(),
                messageContent.trim(),
                LocalDateTime.now()
        );

        Message newMessage = MessageConverter.toDomain(newMessageBean);
        messageDAO.addMessage(newMessage);
    }

    public void addConversation(String description) throws NullFieldException {
        validateConversationFields(description);
        conversationDAO.addConversation(description, currentProjectName);
    }

    public void deleteConversation(String selectedDescription) throws SQLException {
        List<Conversation> conversations = conversationDAO.getConversationsForProject(currentProjectName);
        Conversation conversationToDelete = conversations.stream()
                .filter(c -> c.getDescription().equals(selectedDescription))
                .findFirst()
                .orElse(null);

        if (conversationToDelete == null) {
            throw new SQLException(localizationManager.getText("conversation.delete.error"));
        }

        conversationDAO.deleteConversation(conversationToDelete.getConversationID());
    }

    public List<UserBean> getUsersNotInConversation(Long conversationId) {
        return conversationDAO.getUsersNotInConversation(conversationId)
                .stream()
                .map(UserConverter::toBean)
                .toList();
    }

    public List<UserBean> getUsersInConversation(Long conversationId) {
        return conversationDAO.getUsersInConversation(conversationId)
                .stream()
                .map(UserConverter::toBean)
                .toList();
    }

    public void addUserToConversation(Long conversationId, String username) {
        conversationDAO.addUserToConversation(conversationId, username);
    }

    public void removeUserFromConversation(Long conversationId, String username) {
        conversationDAO.removeUserFromConversation(conversationId, username);
    }

    public void back() {
        applicationController.back();
    }

    private void validateConversationFields(String description) throws NullFieldException {
        if (description == null || description.trim().isEmpty()) {
            throw new NullFieldException("conversation.not.null", localizationManager);
        }
    }


}
