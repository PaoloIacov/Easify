package view.ConversationView.DecoratedConversationView;

import controller.ActionHandler;
import model.bean.ConversationBean;
import model.localization.LocalizationManager;
import view.ConversationView.ConversationView;
import view.ConversationView.GraphicConversationView;
import view.PanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicPmConversationViewDecorator extends ConversationViewDecorator {
    private ActionHandler actionHandler;
    private final LocalizationManager localizationManager;
    private List <ConversationBean> conversationList;
    private ConversationBean selectedConversation;
    private final Map<String, JButton> addEmployeeButtons = new HashMap<>();
    private final Map<String, JButton> removeEmployeeButtons = new HashMap<>();

    public GraphicPmConversationViewDecorator(ConversationView conversationView, LocalizationManager localizationManager) {
        super(conversationView);
        this.localizationManager = localizationManager;
        replaceHeaderWithPmHeader();
        replaceConversationListPanel();
    }

    @Override
    public void displayConversations(List<ConversationBean> conversations) {
        conversationList = conversations;
        replaceConversationListPanel();
    }

    private JButton createButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(_ -> {
            if (actionHandler != null) {
                actionHandler.handleAction(actionCommand);
            }
        });
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

                // Aggiunge il nuovo header per PM/Admin
                JPanel pmHeader = createPmConversationHeader();
                rightPanel.add(pmHeader, BorderLayout.NORTH);

                // Aggiorna la UI
                rightPanel.revalidate();
                rightPanel.repaint();
            } else {
                System.err.println("Errore: rightPanel è null.");
            }
        }
    }

    private JPanel createPmConversationHeader() {
        JPanel pmHeader = PanelUtils.createHeaderPanel();

        JButton addConversationButton = createButton(localizationManager.getText("conversation.button.create"), "4");
        PanelUtils.addButtonToPanel(pmHeader, addConversationButton, 0);

        JButton deleteConversationButton = createButton(localizationManager.getText("conversation.button.delete"), "5");
        PanelUtils.addButtonToPanel(pmHeader, deleteConversationButton, 1);

        JButton backButton = createButton(localizationManager.getText("generic.back"), "3");
        PanelUtils.addButtonToPanel(pmHeader, backButton, 2);

        return pmHeader;
    }

    private void replaceConversationListPanel() {
        if (decoratedConversationView instanceof GraphicConversationView graphicView) {
            JPanel leftPanel = graphicView.getLeftPanel();

            if (leftPanel != null) {
                leftPanel.removeAll();
                if (conversationList != null) {
                    for (ConversationBean conversation : conversationList) {
                        addPmConversationItem(conversation, leftPanel);
                    }
                }
                leftPanel.revalidate();
                leftPanel.repaint();
            }
        }
    }

    private void addPmConversationItem(ConversationBean conversation, JPanel conversationListPanel) {
        JPanel conversationItem = new JPanel();
        conversationItem.setLayout(new BoxLayout(conversationItem, BoxLayout.Y_AXIS));
        conversationItem.setBackground(new Color(42, 46, 54));
        conversationItem.setMaximumSize(new Dimension(300, 80));
        conversationItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(42, 46, 54));

        JLabel titleLabel = new JLabel("<html><b>" + conversation.getDescription() + "</b></html>");
        titleLabel.setForeground(Color.WHITE);

        JButton selectButton = createButton("Select", "1");
        selectButton.addActionListener(_ -> {
            selectedConversation = conversation;
            displayMessages(null);
        });
        JButton addEmployeeButton = createButton("Add User", "6");
        JButton removeEmployeeButton = createButton("Remove User", "7");

        addEmployeeButtons.put(conversation.getDescription(), addEmployeeButton);
        removeEmployeeButtons.put(conversation.getDescription(), removeEmployeeButton);

        addEmployeeButton.setEnabled(false);
        removeEmployeeButton.setEnabled(false);

        selectButton.addActionListener(_ -> {
            selectedConversation = conversation;
            displayMessages(null);

            // Disable all buttons first
            addEmployeeButtons.values().forEach(button -> button.setEnabled(false));
            removeEmployeeButtons.values().forEach(button -> button.setEnabled(false));

            // Enable buttons for the selected conversation
            addEmployeeButton.setEnabled(true);
            removeEmployeeButton.setEnabled(true);
        });

        topPanel.add(titleLabel);
        topPanel.add(selectButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(42, 46, 54));
        bottomPanel.add(addEmployeeButton);
        bottomPanel.add(removeEmployeeButton);

        conversationItem.add(topPanel);
        conversationItem.add(bottomPanel);

        conversationListPanel.add(conversationItem);
        conversationListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        conversationListPanel.revalidate();
        conversationListPanel.repaint();
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

        if (result == JOptionPane.OK_OPTION) {
            return (String) conversationComboBox.getSelectedItem();
        }
        return null;
    }


    public String showAddUserDialog(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    localizationManager.getText("conversation.add.user.empty"),
                    localizationManager.getText("error.title"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Object[] options = usernames.toArray();
        String selectedUser = (String) JOptionPane.showInputDialog(
                null,
                localizationManager.getText("conversation.add.user.prompt"),
                localizationManager.getText("conversation.add.user.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedUser != null && !selectedUser.trim().isEmpty()) {
            return selectedUser;
        } else {
            JOptionPane.showMessageDialog(null,
                    localizationManager.getText("conversation.add.user.cancelled"),
                    localizationManager.getText("error.generic"),
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
    }

    public String showRemoveUserDialog(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    localizationManager.getText("conversation.remove.user.empty"),
                    localizationManager.getText("error.title"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Object[] options = usernames.toArray();
        String selectedUser = (String) JOptionPane.showInputDialog(
                null,
                localizationManager.getText("conversation.remove.user.prompt"),
                localizationManager.getText("conversation.remove.user.title"),
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedUser != null && !selectedUser.trim().isEmpty()) {
            return selectedUser;
        } else {
            JOptionPane.showMessageDialog(null,
                    localizationManager.getText("conversation.remove.user.cancelled"),
                    localizationManager.getText("error.generic"),
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
    }



    @Override
    public boolean isGraphic() {
        return true;
    }

    @Override
    public ConversationBean getSelectedConversation() {
        return selectedConversation;
    }

    @Override
    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        decoratedConversationView.setActionHandler(actionHandler);
    }


}
