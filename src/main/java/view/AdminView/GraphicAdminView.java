package view.AdminView;

import controller.AdminController;
import model.bean.UserBean;
import model.localization.LocalizationManager;
import view.BackgroundPanel;
import view.PanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class GraphicAdminView extends JFrame implements AdminView {

    private final transient LocalizationManager localizationManager;
    private final transient AdminController adminController;
    private JPanel userListPanel;
    private BackgroundPanel userInfoPanel;

    public GraphicAdminView(LocalizationManager localizationManager, AdminController adminController) throws SQLException {
        this.localizationManager = localizationManager;
        this.adminController = adminController;
        initializeUI();
        displayAllUsers();
    }

    private void initializeUI() {
        PanelUtils.configureMainWindow(this, localizationManager.getText("admin.menu.title"));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createLeftPanel() {
        userListPanel = new JPanel();
        return PanelUtils.createLeftPanel(userListPanel);
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel userHeader = createUserHeader();
        rightPanel.add(userHeader, BorderLayout.NORTH);

        userInfoPanel = PanelUtils.createBackgroundPanel("background.jpg");

        addNavigateToProjectsButton();

        rightPanel.add(userInfoPanel, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel createUserHeader() {
        JPanel userHeader = PanelUtils.createHeaderPanel();

        JButton addUserButton = createButton(localizationManager.getText("admin.add.user"), this::addUser);
        PanelUtils.addButtonToPanel(userHeader, addUserButton, 0);

        JButton deleteUserButton = createButton(localizationManager.getText("admin.remove.user"), this::removeUser);
        PanelUtils.addButtonToPanel(userHeader, deleteUserButton, 1);

        JButton backButton = createButton(localizationManager.getText("generic.back"), this::back);
        PanelUtils.addButtonToPanel(userHeader, backButton, 2);

        return userHeader;
    }

    private void addNavigateToProjectsButton() {
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        userInfoPanel.add(createNavigateToProjectsButton());
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }


    public void addUserItem(UserBean user) {
        JPanel userItem = PanelUtils.createStyledPanel();
        userItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        userItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("<html><b>" + user.getUsername() + "</b></html>");
        titleLabel.setForeground(Color.WHITE);

        JButton selectButton = createButton(localizationManager.getText("generic.select"), () -> {
            setSelectedUsername(user.getUsername());
            displayUserDetails(user);
        });

        userItem.add(titleLabel);
        userItem.add(selectButton);

        userListPanel.add(userItem);
        userListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        refresh();
    }

    @Override
    public void displayAllUsers() throws SQLException {
        userListPanel.removeAll();
        List<UserBean> users = adminController.getAllUsers();
        users.forEach(this::addUserItem);
        refresh();
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void displayUserDetails(UserBean user) {
        userInfoPanel.removeAll();
        userInfoPanel.setBackground(new Color(30, 33, 40));

        if (user != null) {
            JPanel userDetailsPanel = new JPanel();
            userDetailsPanel.setLayout(new GridBagLayout());
            userDetailsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
            userDetailsPanel.setBackground(new Color(45, 50, 60));
            userDetailsPanel.setPreferredSize(new Dimension(350, 200));
            userDetailsPanel.setMaximumSize(new Dimension(350, 250));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 10, 5, 10); // Padding tra gli elementi
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel usernameLabel = createStyledLabel("Username: ", user.getUsername());
            JLabel roleLabel = createStyledLabel("Role: ", String.valueOf(user.getRole()));
            JLabel nameLabel = createStyledLabel("Name: ", user.getName());
            JLabel surnameLabel = createStyledLabel("Surname: ", user.getSurname());

            userDetailsPanel.add(usernameLabel, gbc);
            gbc.gridy++;
            userDetailsPanel.add(roleLabel, gbc);
            gbc.gridy++;
            userDetailsPanel.add(nameLabel, gbc);
            gbc.gridy++;
            userDetailsPanel.add(surnameLabel, gbc);

            userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
            userInfoPanel.add(Box.createVerticalStrut(20));
            userInfoPanel.add(userDetailsPanel);
            userInfoPanel.add(Box.createVerticalStrut(20));

        } else {
            JLabel noUserLabel = new JLabel("User not found.");
            noUserLabel.setForeground(Color.WHITE);
            noUserLabel.setFont(new Font("Arial", Font.BOLD, 16));
            userInfoPanel.add(noUserLabel);
        }

        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        userInfoPanel.add(createNavigateToProjectsButton());
        refresh();
    }

    private JLabel createStyledLabel(String label, String value) {
        JLabel styledLabel = new JLabel("<html><b style='color: #A0C0FF;'>" + label + "</b><span style='color: white;'>" + value + "</span></html>");
        styledLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        return styledLabel;
    }

    @Override
    public void addUser() {
        try {
            String username = JOptionPane.showInputDialog(localizationManager.getText("user.username"));
            String password = JOptionPane.showInputDialog(localizationManager.getText("user.password"));
            String name = JOptionPane.showInputDialog(localizationManager.getText("user.name"));
            String surname = JOptionPane.showInputDialog(localizationManager.getText("user.surname"));
            int role = Integer.parseInt(JOptionPane.showInputDialog(localizationManager.getText("user.role")));

            UserBean user = new UserBean(username, password, name, surname, role);

            adminController.addUser(user);
            showSuccess(localizationManager.getText("generic.action.success"));
            displayAllUsers();
        } catch (NumberFormatException e) {
            showError(localizationManager.getText("admin.add.user.invalid.role"));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @Override
    public void removeUser() {
        try {
            List<UserBean> users = adminController.getAllUsers();
            List<String> usernames = users.stream().map(UserBean::getUsername).toList();

            if (usernames.isEmpty()) {
                showError(localizationManager.getText("admin.remove.no.users"));
                return;
            }

            JComboBox<String> userDropdown = new JComboBox<>(usernames.toArray(new String[0]));

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel(localizationManager.getText("admin.remove.user.prompt")));
            panel.add(userDropdown);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    localizationManager.getText("admin.remove.user.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String selectedUser = (String) userDropdown.getSelectedItem();
                if (selectedUser != null) {
                    adminController.removeUser(selectedUser);
                    showSuccess(localizationManager.getText("generic.action.success"));
                    displayAllUsers();
                }
            }
        } catch (SQLException e) {
            showError(localizationManager.getText("admin.remove.error") + ": " + e.getMessage());
        }
    }


    @Override
    public String getInput(String promptKey) {
        return JOptionPane.showInputDialog(this, localizationManager.getText(promptKey));
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, localizationManager.getText("error.title"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, localizationManager.getText("success.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean isGraphic() {
        return true;
    }

    @Override
    public void refresh() {
        revalidate();
        repaint();
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void back() {
        adminController.back();
    }

    public void setSelectedUsername(String username) {

        userInfoPanel.removeAll();
        if (username != null && !username.isEmpty()) {
            JLabel userDetailsLabel = new JLabel("<html><h1>User: " + username + "</h1></html>");
            userDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userInfoPanel.add(userDetailsLabel);
        }
        refresh();
    }

    public JButton createNavigateToProjectsButton() {
        JButton navigateToProjectsButton = createButton(localizationManager.getText("admin.menu.navigate.project"), adminController::navigateToProjectView);
        navigateToProjectsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        navigateToProjectsButton.setPreferredSize(new Dimension(200, 40));
        navigateToProjectsButton.addActionListener(_ -> {
        });

        return navigateToProjectsButton;
    }

}
