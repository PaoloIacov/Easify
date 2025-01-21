package view.LoginView;

import model.bean.CredentialsBean;
import model.localization.LocalizationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class GraphicLoginView extends JFrame implements LoginView {

    private final transient LocalizationManager localizationManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton submitButton;

    public GraphicLoginView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;

        // Impostazioni della finestra principale
        setTitle(localizationManager.getText("login.title"));
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Pannello principale con layout e sfondo
        JPanel mainPanel = new JPanel() {
            private final transient Image backgroundImage = loadBackgroundImage("background.jpg");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 20, 50, 20));
        mainPanel.setOpaque(false);

        // Icona utente
        addUserIcon(mainPanel, "userIcon.png");

        // Campo username
        usernameField = createTextField(localizationManager.getText("login.username"));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Campo password
        passwordField = createPasswordField(localizationManager.getText("login.password"));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Pulsante Submit
        submitButton = createSubmitButton(localizationManager.getText("login.submit"));
        mainPanel.add(submitButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        setContentPane(mainPanel);
        setVisible(true);
    }

    private Image loadBackgroundImage() {
        ImageIcon icon = createImageIcon("background.jpg");
        return (icon != null) ? icon.getImage() : null;
    }

    private void addUserIcon(JPanel panel) {
        ImageIcon icon = createImageIcon("userIcon.jpg");
        if (icon != null) {
            Image scaledIcon = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel userIcon = new JLabel(new ImageIcon(scaledIcon));
            userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(userIcon);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
    }

    private JButton createSubmitButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(34, 177, 76));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        return button;
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public CredentialsBean getCredentialsInput() {
        return new CredentialsBean(usernameField.getText(), new String(passwordField.getPassword()));
    }

    @Override
    public void showError(String messageKey) {
        JOptionPane.showMessageDialog(this, localizationManager.getText(messageKey),
                localizationManager.getText("error.title"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showSuccess(String messageKey) {
        JOptionPane.showMessageDialog(this, localizationManager.getText(messageKey),
                localizationManager.getText("success.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void refresh() {
        setTitle(localizationManager.getText("login.title"));
        usernameField.setBorder(BorderFactory.createTitledBorder(localizationManager.getText("login.username")));
        passwordField.setBorder(BorderFactory.createTitledBorder(localizationManager.getText("login.password")));
        submitButton.setText(localizationManager.getText("login.submit"));
        repaint();
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void back() {
        JOptionPane.showMessageDialog(this, localizationManager.getText("login.back"), "Back", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setSubmitButtonListener(ActionListener listener) {
        for (ActionListener al : submitButton.getActionListeners()) {
            submitButton.removeActionListener(al);
        }
        submitButton.addActionListener(listener);
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(15);
        setupPlaceholder(textField, placeholder);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(15);
        setupPlaceholder(passwordField, placeholder);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return passwordField;
    }

    private void setupPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }

    private void setupPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.setEchoChar((char) 0);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
    }

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
