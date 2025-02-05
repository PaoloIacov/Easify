package view.ConversationView;

import controller.ActionHandler;
import model.bean.ConversationBean;
import model.bean.MessageBean;
import model.localization.LocalizationManager;
import view.BackgroundPanel;
import view.PanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GraphicConversationView extends JFrame implements ConversationView {

    private final transient LocalizationManager localizationManager;
    private transient ActionHandler actionHandler;
    private JPanel conversationListPanel;
    private BackgroundPanel messagePanel;
    private JTextField messageInput;
    private ConversationBean selectedConversation;
    private JPanel rightPanel;
    private JPanel leftPanel;

    public GraphicConversationView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
        initializeUI();
    }

    private void initializeUI() {
        setTitle(localizationManager.getText("conversation.view.title"));
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();

        PanelUtils.setupMainLayout(this, leftPanel, rightPanel);
    }


    private JButton createButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.addActionListener(_ -> {
            if (actionHandler != null) {
                actionHandler.handleAction(actionCommand);
            }
        });
        return button;
    }

    private JPanel createLeftPanel() {
        conversationListPanel = new JPanel();
        return PanelUtils.createLeftPanel(conversationListPanel);
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());

        JPanel conversationHeader = createConversationHeader();
        rightPanel.add(conversationHeader, BorderLayout.NORTH);

        messagePanel = new BackgroundPanel("background.jpg");
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane messageScrollPane = new JScrollPane(messagePanel);
        messageScrollPane.setBorder(null);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);

        JPanel messageInputPanel = createMessageInputPanel();
        rightPanel.add(messageInputPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JPanel createConversationHeader() {
        JPanel conversationHeader = PanelUtils.createHeaderPanel();

        JButton backButton = createButton(localizationManager.getText("generic.back"), "3");
        PanelUtils.addButtonToPanel(conversationHeader, backButton, 0);

        return conversationHeader;
    }

    private JPanel createMessageInputPanel() {
        JPanel messageInputPanel = new JPanel();
        messageInputPanel.setLayout(new BorderLayout());
        messageInputPanel.setBackground(Color.WHITE);
        messageInputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        messageInput = new JTextField();
        JButton sendButton = createButton(localizationManager.getText("conversation.button.send"), "2");

        messageInputPanel.add(messageInput, BorderLayout.CENTER);
        messageInputPanel.add(sendButton, BorderLayout.EAST);

        return messageInputPanel;
    }

    @Override
    public void displayConversations(List<ConversationBean> conversations) {
        conversationListPanel.removeAll();
        for (ConversationBean conversation : conversations) {
            addConversationItem(conversation);
        }
        conversationListPanel.revalidate();
        conversationListPanel.repaint();
    }

    private void addConversationItem(ConversationBean conversation) {
        JPanel conversationItem = new JPanel();
        conversationItem.setLayout(new BoxLayout(conversationItem, BoxLayout.Y_AXIS));
        conversationItem.setBackground(new Color(42, 46, 54));
        conversationItem.setMaximumSize(new Dimension(250, 50));
        conversationItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("<html><b>" + conversation.getDescription() + "</b></html>");
        titleLabel.setForeground(Color.WHITE);

        JButton selectButton = createButton(localizationManager.getText("generic.select"), "1");
        selectButton.addActionListener(_ -> {
            selectedConversation = conversation;
            displayMessages(null);
            });

        conversationItem.add(titleLabel);
        conversationItem.add(selectButton);

        conversationListPanel.add(conversationItem);
    }

    @Override
    public void displayMessages(List<MessageBean> messages) {
        messagePanel.removeAll();
        if (messages != null) {
            for (MessageBean message : messages) {
                addMessageItem(message.getSenderUsername(), message.getContent(), !message.getSenderUsername().equals("You"));
            }
        }
        refresh();
    }

    private void addMessageItem(String sender, String message, boolean isReceived) {
        JPanel messageItem = new JPanel();
        messageItem.setLayout(new BoxLayout(messageItem, BoxLayout.Y_AXIS));
        messageItem.setBorder(new EmptyBorder(10, 10, 10, 10));
        messageItem.setMaximumSize(new Dimension(400, 80));

        JLabel messageLabel = new JLabel("<html><b>" + sender + ":</b><br/><p style='width: 250px;'>" + message + "</p></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        if (isReceived) {
            messageItem.setBackground(new Color(220, 248, 198)); // Verde chiaro (come WhatsApp ricevuto)
            messageLabel.setForeground(Color.BLACK);
            messageItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            messageItem.setBackground(new Color(187, 222, 251)); // Blu chiaro (come WhatsApp inviato)
            messageLabel.setForeground(Color.BLACK);
            messageItem.setAlignmentX(Component.RIGHT_ALIGNMENT);
        }

        messageItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        messageItem.add(messageLabel);
        messagePanel.add(messageItem);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 8))); // Spaziatura tra messaggi

        refresh();
    }

    @Override
    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            addMessageItem("You", message, false);
            resetMessageInput();
        }
    }

    @Override
    public ConversationBean getSelectedConversation() {
        return selectedConversation;
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, localizationManager.getText("success.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, localizationManager.getText("error.title"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        this.actionHandler = handler;
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void refresh() {
        revalidate();
        repaint();
    }

    @Override
    public String getMessageInput() {
        return messageInput.getText();
    }

    @Override
    public void resetMessageInput() {
        messageInput.setText(" ");
    }

    @Override
    public boolean isGraphic() {
        return true;
    }

    @Override
    public String getInput(String promptKey) {
        return JOptionPane.showInputDialog(this, localizationManager.getText(promptKey));
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    @Override
    public void back() {
        dispose();
    }
}
