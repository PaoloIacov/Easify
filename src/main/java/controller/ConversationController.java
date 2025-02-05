package controller;

import controller.exceptions.NullFieldException;
import model.bean.ConversationBean;
import model.bean.CredentialsBean;
import model.bean.MessageBean;
import model.converter.ConversationConverter;
import model.converter.MessageConverter;
import model.dao.ConversationDAO;
import model.dao.MessageDAO;
import model.domain.Conversation;
import model.domain.Message;
import model.domain.User;
import model.localization.LocalizationManager;
import view.ConversationView.ConversationView;
import view.ConversationView.DecoratedConversationView.CliPmConversationViewDecorator;
import view.ConversationView.DecoratedConversationView.GraphicPmConversationViewDecorator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ConversationController implements ActionHandler {

    private final ConversationView conversationView;
    private final ConversationDAO conversationDAO;
    private final MessageDAO messageDAO;
    private final LocalizationManager localizationManager;
    private ConversationBean selectedConversation;
    private final CredentialsBean loggedInUser;
    private final ApplicationController applicationController;
    private String currentProjectName;
    private static final String LOADING_ERROR = "conversation.error.loading";
    private static final String CONVO_NO_SELECTED = "conversation.no.selected";

    public ConversationController(ConversationView conversationView, ConversationDAO conversationDAO, MessageDAO messageDAO, LocalizationManager localizationManager, CredentialsBean loggedInUser, ApplicationController applicationController) {
        this.conversationView = conversationView;
        this.conversationDAO = conversationDAO;
        this.messageDAO = messageDAO;
        this.localizationManager = localizationManager;
        this.loggedInUser = loggedInUser;
        this.applicationController = applicationController;

        if (conversationView.isGraphic()) {
            conversationView.setActionHandler(this);
        }
    }

    public void start(String projectName) {
        this.currentProjectName = projectName;
        displayConversationsForUser(loggedInUser.getUsername(), loggedInUser.getRole());
        if (!conversationView.isGraphic()) {
            runCliLoop();
        } else {
            conversationView.display();
        }
    }

    private void runCliLoop() {
        boolean running = true;
        while (running) {
            try {
                conversationView.display();
                String choice = conversationView.getInput("conversation.menu.prompt");
                int action = Integer.parseInt(choice);
                if (action<1 || action>3 && loggedInUser.getRole() == 1) {
                    conversationView.showError("error.invalid.option");
                    continue;
                }
                running = handleAction(choice);
            } catch (Exception e) {
                conversationView.showError(LOADING_ERROR);
            }
        }
    }

    @Override
    public boolean handleAction(String action) {
        try {
            switch (action) {
                case "1":
                    displayMessagesForSelectedConversation();
                    break;
                case "2":
                    handleSendMessage(loggedInUser);
                    break;
                case "3":
                    applicationController.back();
                    return false;
                case "4":
                    handleAddConversation();
                    break;
                case "5":
                    handleDeleteConversation();
                    break;
                case "6":
                    handleAddUserToConversation();
                    break;
                case "7":
                    handleRemoveUserFromConversation();
                    break;
                default:
                    conversationView.showError(localizationManager.getText("conversation.option.invalid"));
                    return false;
            }
        } catch (Exception e) {
            conversationView.showError(localizationManager.getText(LOADING_ERROR) + ": " + e.getMessage());
        }
        return true;
    }

    private void displayMessagesForSelectedConversation() {
        try {
            selectedConversation = conversationView.getSelectedConversation();
            if (selectedConversation == null) {
                conversationView.showError(CONVO_NO_SELECTED);
                return;
            }

            List<MessageBean> messages = messageDAO.getMessagesByConversationID(selectedConversation.getConversationID())
                    .stream()
                    .map(MessageConverter::toBean)
                    .toList();

            conversationView.displayMessages(messages);
        } catch (Exception e) {
            conversationView.showError(localizationManager.getText(LOADING_ERROR) + ": " + e.getMessage());
        }
    }

    private void handleSendMessage(CredentialsBean loggedInUser) {
        try {
            selectedConversation = conversationView.getSelectedConversation();
            if (selectedConversation == null) {
                conversationView.showError(CONVO_NO_SELECTED);
                return;
            }

            String messageContent = conversationView.getMessageInput();

            if (messageContent == null || messageContent.trim().isEmpty()) {
                conversationView.showError(localizationManager.getText("conversation.error.empty.message"));
                return;
            }

            MessageBean newMessageBean = new MessageBean(
                    selectedConversation.getConversationID(),
                    loggedInUser.getUsername(),
                    messageContent.trim(),
                    LocalDateTime.now()
            );

            Message newMessage = MessageConverter.toDomain(newMessageBean);

            messageDAO.addMessage(newMessage);

            conversationView.resetMessageInput();
            displayMessagesForSelectedConversation();

        } catch (Exception e) {
            conversationView.showError(localizationManager.getText("conversation.error.sending") + ": " + e.getMessage());
        }
    }


    private void displayConversationsForUser(String username, int role) {
        try {
            List<ConversationBean> conversations;
            if (role == 2 || role == 1) {
                conversations = conversationDAO.getConversationsForUser(username, role)
                        .stream()
                        .map(ConversationConverter::toBean)
                        .toList();
            } else {
                conversations = conversationDAO.getConversationsForProject(currentProjectName)
                        .stream()
                        .map(ConversationConverter::toBean)
                        .toList();
            }
            conversationView.displayConversations(conversations);
        } catch (Exception e) {
            conversationView.showError(localizationManager.getText(LOADING_ERROR) + ": " + e.getMessage());
        }
    }

    private void handleAddConversation() {
        try {
            String description = conversationView.getInput(localizationManager.getText("conversation.add.prompt.description"));
            if (description == null || description.trim().isEmpty()) {
                conversationView.showError(localizationManager.getText("conversation.add.error.empty.description"));
                return;
            }

            validateConversationFields(description);
            conversationDAO.addConversation(description, currentProjectName);
            conversationView.showSuccess(localizationManager.getText("conversation.add.success"));

            List<ConversationBean> updatedConversations = conversationDAO.getConversationsForProject(currentProjectName)
                    .stream()
                    .map(ConversationConverter::toBean)
                    .toList();
            conversationView.displayConversations(updatedConversations);

        } catch (Exception e) {
            conversationView.showError(localizationManager.getText("conversation.add.error") + ": " + e.getMessage());
        }
    }

    private void handleDeleteConversation() {
        try {
            if (!(conversationView instanceof CliPmConversationViewDecorator) &&
                    !(conversationView instanceof GraphicPmConversationViewDecorator)) {
                conversationView.showError(localizationManager.getText("conversation.delete.permission.denied"));
                return;
            }

            List<Conversation> conversations = conversationDAO.getConversationsForProject(currentProjectName);
            List<String> conversationDescriptions = conversations.stream()
                    .map(Conversation::getDescription)
                    .toList();

            if (conversationDescriptions.isEmpty()) {
                conversationView.showError(localizationManager.getText("conversation.list.empty"));
                return;
            }

            String selectedDescription;
            switch (conversationView) {
                case CliPmConversationViewDecorator cliPmView -> selectedDescription = cliPmView.handleDeleteConversation(conversationDescriptions);
                case GraphicPmConversationViewDecorator graphicPmView -> selectedDescription = graphicPmView.showDeleteConversationDialog(conversationDescriptions);
                default -> selectedDescription = null;
            }

            if (selectedDescription == null) {
                return;
            }

            Conversation conversationToDelete = conversations.stream()
                    .filter(c -> c.getDescription().equals(selectedDescription))
                    .findFirst()
                    .orElse(null);

            if (conversationToDelete == null) {
                conversationView.showError(localizationManager.getText("conversation.delete.error"));
                return;
            }

            // Elimina la conversazione dal database
            conversationDAO.deleteConversation(conversationToDelete.getConversationID());
            conversationView.showSuccess(localizationManager.getText("conversation.delete.success"));

            // Aggiorna la visualizzazione
            displayConversationsForProject(currentProjectName);

        } catch (Exception e) {
            conversationView.showError(localizationManager.getText("conversation.delete.error") + ": " + e.getMessage());
        }
    }

    private void displayConversationsForProject(String projectName) {
        try {
            List<ConversationBean> conversations = conversationDAO.getConversationsForProject(projectName)
                    .stream()
                    .map(ConversationConverter::toBean)
                    .toList();
            conversationView.displayConversations(conversations);
        } catch (SQLException e) {
            conversationView.showError(localizationManager.getText(LOADING_ERROR) + ": " + e.getMessage());
        }
    }

    private void validateConversationFields(String description) throws NullFieldException {
        if (description == null || description.trim().isEmpty()) {
            throw new NullFieldException("conversation.not.null", localizationManager);
        }
    }

    private void handleAddUserToConversation() {
        try {
            if (!(conversationView instanceof CliPmConversationViewDecorator) &&
                    !(conversationView instanceof GraphicPmConversationViewDecorator)) {
                conversationView.showError(localizationManager.getText("conversation.add.user.permission.denied"));
                return;
            }

            ConversationBean convo = conversationView.getSelectedConversation();
            if (convo == null) {
                conversationView.showError(CONVO_NO_SELECTED);
                return;
            }

            List<User> users = conversationDAO.getUsersNotInConversation(convo.getConversationID());
            List<String> usernames = users.stream().map(User::getUsername).toList();

            if (usernames.isEmpty()) {
                conversationView.showError(localizationManager.getText("conversation.add.user.empty"));
                return;
            }

            String selectedUser = (conversationView instanceof CliPmConversationViewDecorator cliPmView)
                    ? cliPmView.showAddUserDialog(usernames)
                    : ((GraphicPmConversationViewDecorator) conversationView).showAddUserDialog(usernames);

            if (selectedUser == null) {
                return;
            }

            conversationDAO.addUserToConversation(selectedConversation.getConversationID(), selectedUser);
            conversationView.showSuccess(localizationManager.getText("conversation.add.user.success"));

        } catch (Exception e) {
            conversationView.showError("conversation.add.user.error");
        }
    }


    private void handleRemoveUserFromConversation() {
        try {
            if (!(conversationView instanceof CliPmConversationViewDecorator) &&
                    !(conversationView instanceof GraphicPmConversationViewDecorator)) {
                conversationView.showError(localizationManager.getText("conversation.remove.user.permission.denied"));
                return;
            }

            ConversationBean convo = conversationView.getSelectedConversation();
            if (convo == null) {
                conversationView.showError(CONVO_NO_SELECTED);
                return;
            }

            List<User> users = conversationDAO.getUsersInConversation(convo.getConversationID());
            List<String> usernames = users.stream().map(User::getUsername).toList();

            if (usernames.isEmpty()) {
                conversationView.showError("conversation.remove.user.empty");
                return;
            }

            // Usa il metodo corretto del Decorator
            String selectedUser = (conversationView instanceof CliPmConversationViewDecorator cliPmView)
                    ? cliPmView.showRemoveUserDialog(usernames)
                    : ((GraphicPmConversationViewDecorator) conversationView).showRemoveUserDialog(usernames);

            if (selectedUser == null) {
                return;
            }

            conversationDAO.removeUserFromConversation(selectedConversation.getConversationID(), selectedUser);
            conversationView.showSuccess(localizationManager.getText("conversation.remove.user.success"));

        } catch (Exception e) {
            conversationView.showError(localizationManager.getText("conversation.remove.user.error") + ": " + e.getMessage());
        }
    }


}
