package view.ConversationView.DecoratedConversationView;

import controller.ConversationController;
import controller.exceptions.NullFieldException;
import model.bean.ConversationBean;
import model.bean.UserBean;
import model.localization.LocalizationManager;
import view.ConversationView.ConversationView;
import view.ConversationView.GraphicConversationView;
import view.PanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicPmConversationViewDecorator extends ConversationViewDecorator {

    private final LocalizationManager localizationManager;
    private final ConversationController conversationController;
    private ConversationBean selectedConversation;
    private final Map<String, JButton> addUserButtons = new HashMap<>();
    private final Map<String, JButton> removeUserButtons = new HashMap<>();
    private static final String GENERIC_ERROR = "generic.error";

    public GraphicPmConversationViewDecorator(ConversationView conversationView, LocalizationManager localizationManager, ConversationController conversationController) {
        super(conversationView);
        this.localizationManager = localizationManager;
        this.conversationController = conversationController;
        replaceHeaderWithPmHeader();
        replaceConversationListPanel();
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(_ -> action.run());
        return button;
    }

    private void replaceHeaderWithPmHeader() {
        if (decoratedConversationView instanceof GraphicConversationView graphicView) {
            JPanel rightPanel = graphicView.getRightPanel();

            if (rightPanel != null) {
                Component oldHeader = ((BorderLayout) rightPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);

                if (oldHeader != null) {
                    rightPanel.remove(oldHeader);
                }

                JPanel pmHeader = createPmConversationHeader();
                rightPanel.add(pmHeader, BorderLayout.NORTH);

                rightPanel.revalidate();
                rightPanel.repaint();
            } else {
                System.err.println("Error: rightPanel is null.");
            }
        }
    }

    private JPanel createPmConversationHeader() {
        JPanel pmHeader = new JPanel(new GridLayout(1, 3, 15, 10)); // Griglia con 3 colonne, spaziatura tra bottoni
        pmHeader.setBackground(Color.WHITE);
        pmHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding attorno al pannello

        JButton addConversationButton = createButton(localizationManager.getText("conversation.button.create"), this::addConversation);
        JButton deleteConversationButton = createButton(localizationManager.getText("conversation.button.delete"), this::removeConversation);
        JButton backButton = createButton(localizationManager.getText("generic.back"), this::back);

        // Imposta dimensioni minime per evitare il taglio del testo
        Dimension buttonSize = new Dimension(160, 40);
        addConversationButton.setPreferredSize(buttonSize);
        deleteConversationButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        pmHeader.add(addConversationButton);
        pmHeader.add(deleteConversationButton);
        pmHeader.add(backButton);

        return pmHeader;
    }


    private void replaceConversationListPanel() {
        try {
        if (decoratedConversationView instanceof GraphicConversationView graphicView) {
            JPanel leftPanel = graphicView.getLeftPanel();

            if (leftPanel != null) {
                leftPanel.removeAll();
                List<ConversationBean> conversationList = conversationController.getConversationsForUser();
                if (conversationList != null) {
                    for (ConversationBean conversation : conversationList) {
                        addPmConversationItem(conversation, leftPanel);
                    }
                }
                leftPanel.revalidate();
                leftPanel.repaint();
            }
        }
        } catch (SQLException e) {
            showError(localizationManager.getText(GENERIC_ERROR));
        }
    }

    private void addPmConversationItem(ConversationBean conversation, JPanel conversationListPanel) {
        JPanel conversationItem = PanelUtils.createStyledPanel();
        conversationItem.setMaximumSize(new Dimension(300, 80));
        conversationItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(42, 46, 54));

        JLabel titleLabel = new JLabel("<html><b>" + conversation.getDescription() + "</b></html>");
        titleLabel.setForeground(Color.WHITE);

        JButton selectButton = createButton(localizationManager.getText("generic.select"), () -> selectConversation(conversation));
        JButton addUserButton = createButton(localizationManager.getText("conversation.button.add.user"), this::addUserToConversation);
        JButton removeUserButton = createButton(localizationManager.getText("conversation.button.remove.user"), this::removeUserFromConversation);

        addUserButtons.put(conversation.getDescription(), addUserButton);
        removeUserButtons.put(conversation.getDescription(), removeUserButton);

        addUserButton.setEnabled(false);
        removeUserButton.setEnabled(false);

        selectButton.addActionListener(_ -> {
            selectedConversation = conversation;
            displayMessages(selectedConversation);

            addUserButtons.values().forEach(button -> button.setEnabled(false));
            removeUserButtons.values().forEach(button -> button.setEnabled(false));

            addUserButton.setEnabled(true);
            removeUserButton.setEnabled(true);
        });

        topPanel.add(titleLabel);
        topPanel.add(selectButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(42, 46, 54));
        bottomPanel.add(addUserButton);
        bottomPanel.add(removeUserButton);

        conversationItem.add(topPanel);
        conversationItem.add(bottomPanel);

        conversationListPanel.add(conversationItem);
        conversationListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        conversationListPanel.revalidate();
        conversationListPanel.repaint();
    }

    private void selectConversation(ConversationBean conversation) {
        this.selectedConversation = conversation;
        displayMessages(selectedConversation);
    }

    private void addConversation() {
        try {
            String description = JOptionPane.showInputDialog(localizationManager.getText("conversation.add.prompt.description"));
            if (description != null && !description.trim().isEmpty()) {
                conversationController.addConversation(description);
                displayConversations();
            }
            displayConversations();
        } catch (NullFieldException e) {
            showError(localizationManager.getText("conversation.add.error"));
        } catch (SQLException e) {
            showError(localizationManager.getText(GENERIC_ERROR));
        }

    }

    private void removeConversation() {
        try {
            List<ConversationBean> conversationList = conversationController.getConversationsForUser();
            List<String> conversationDescriptions = conversationList.stream()
                    .map(ConversationBean::getDescription)
                    .toList();

            String selectedConvo = showDeleteConversationDialog(conversationDescriptions);
            if (selectedConversation != null) {
                conversationController.deleteConversation(selectedConvo);
                displayConversations();
            }
        } catch (SQLException e) {
            showError(localizationManager.getText(GENERIC_ERROR));
        }
    }

    private void addUserToConversation() {
        if (selectedConversation == null) {
            showError(localizationManager.getText("conversation.no.selected"));
            return;
        }

        try {
            // Ottieni gli utenti non presenti nella conversazione
            List<String> availableUsers = conversationController.getUsersNotInConversation(selectedConversation.getConversationID())
                    .stream()
                    .map(UserBean::getUsername)
                    .toList();

            if (availableUsers.isEmpty()) {
                showError(localizationManager.getText("conversation.add.user.empty"));
                return;
            }

            // Mostra il dropdown menu per selezionare un utente
            String selectedUser = showAddUserDialog(availableUsers);

            if (selectedUser != null) {
                conversationController.addUserToConversation(selectedConversation.getConversationID(), selectedUser);
                showSuccess(localizationManager.getText("conversation.add.user.success"));
            }
        } catch (RuntimeException e) {
            showError(localizationManager.getText("conversation.add.user.error") + ": " + e.getMessage());
        }
    }

    private void removeUserFromConversation() {
        if (selectedConversation == null) {
            showError(localizationManager.getText("conversation.no.selected"));
            return;
        }

        try {
            // Ottieni gli utenti presenti nella conversazione
            List<String> usersInConversation = conversationController.getUsersInConversation(selectedConversation.getConversationID())
                    .stream()
                    .map(UserBean::getUsername)
                    .toList();

            if (usersInConversation.isEmpty()) {
                showError(localizationManager.getText("conversation.remove.user.empty"));
                return;
            }

            // Mostra il dropdown menu per selezionare un utente da rimuovere
            String selectedUser = showRemoveUserDialog(usersInConversation);

            if (selectedUser != null) {
                conversationController.removeUserFromConversation(selectedConversation.getConversationID(), selectedUser);
                showSuccess(localizationManager.getText("conversation.remove.user.success"));
            }
        } catch (RuntimeException e) {
            showError(localizationManager.getText("conversation.remove.user.error") + ": " + e.getMessage());
        }
    }

    public String showAddUserDialog(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    localizationManager.getText("conversation.add.user.empty"),
                    localizationManager.getText("error.title"),
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        JComboBox<String> userDropdown = new JComboBox<>(usernames.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(localizationManager.getText("conversation.add.user.prompt")));
        panel.add(userDropdown);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                localizationManager.getText("conversation.add.user.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return (result == JOptionPane.OK_OPTION) ? (String) userDropdown.getSelectedItem() : null;
    }

    public String showRemoveUserDialog(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    localizationManager.getText("conversation.remove.user.empty"),
                    localizationManager.getText("error.title"),
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        JComboBox<String> userDropdown = new JComboBox<>(usernames.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(localizationManager.getText("conversation.remove.user.prompt")));
        panel.add(userDropdown);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                localizationManager.getText("conversation.remove.user.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return (result == JOptionPane.OK_OPTION) ? (String) userDropdown.getSelectedItem() : null;
    }

    public String showDeleteConversationDialog(List<String> conversationDescriptions) {
        JComboBox<String> conversationComboBox = new JComboBox<>(conversationDescriptions.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(localizationManager.getText("conversation.delete.prompt")));
        panel.add(conversationComboBox);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                localizationManager.getText("conversation.delete.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return (result == JOptionPane.OK_OPTION) ? (String) conversationComboBox.getSelectedItem() : null;
    }

    @Override
    public boolean isGraphic() {
        return true;
    }

    @Override
    public ConversationBean getSelectedConversation() {
        return selectedConversation;
    }
}
