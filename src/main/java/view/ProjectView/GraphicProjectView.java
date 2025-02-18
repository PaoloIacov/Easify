package view.ProjectView;

import controller.ApplicationController;
import controller.ProjectController;
import model.bean.CredentialsBean;
import model.bean.UserBean;
import model.bean.ProjectBean;
import model.localization.LocalizationManager;
import view.BackgroundPanel;
import view.PanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicProjectView extends JFrame implements ProjectView {
    private final transient LocalizationManager localizationManager;
    private final transient ProjectController projectController;
    private final CredentialsBean loggedInUser;
    private final transient ApplicationController applicationController;
    private JPanel projectListPanel;
    private BackgroundPanel projectInfoPanel;
    private String selectedProjectName;
    private JPanel rightPanel;
    private final Map<String, JButton> addUserButtons = new HashMap<>();
    private final Map<String, JButton> removeUserButtons = new HashMap<>();
    private static final String PROJECT_NO_SELECTED = "project.no.selected";

    public GraphicProjectView(LocalizationManager localizationManager, ProjectController projectController, CredentialsBean loggedInUser, ApplicationController applicationController) {
        this.localizationManager = localizationManager;
        this.projectController = projectController;
        this.loggedInUser = loggedInUser;
        this.applicationController = applicationController;
        initializeUI();
        displayProjects();
    }

    private void initializeUI() {
        PanelUtils.configureMainWindow(this, localizationManager.getText("project.view.title"));
        JPanel leftPanel = createLeftPanel();
        rightPanel = createRightPanel();
        PanelUtils.setupMainLayout(this, leftPanel, rightPanel);
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void displayProjects() {
        try {
            List<ProjectBean> projects = projectController.getProjectsForUser(loggedInUser.getUsername());
            projectListPanel.removeAll();
            for (ProjectBean project : projects) {
                addProjectItem(project.getName());
            }
            projectListPanel.revalidate();
            projectListPanel.repaint();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private JPanel createLeftPanel() {
        projectListPanel = new JPanel();
        return PanelUtils.createLeftPanel(projectListPanel);
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        JPanel projectHeader = createProjectHeader();
        rightPanel.add(projectHeader, BorderLayout.NORTH);
        projectInfoPanel = PanelUtils.createBackgroundPanel("background.jpg");
        updateProjectInfoPanel();
        JScrollPane messageScrollPane = new JScrollPane(projectInfoPanel);
        messageScrollPane.setBorder(null);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);
        return rightPanel;
    }

    private JPanel createProjectHeader() {
        JPanel projectHeader = PanelUtils.createHeaderPanel();
        JButton backButton = createButton(localizationManager.getText("generic.back"), this::back);
        PanelUtils.addButtonToPanel(projectHeader, backButton, 0);
        return projectHeader;
    }

    private void updateProjectInfoPanel() {
        projectInfoPanel.removeAll();
        if (selectedProjectName != null) {
            addLabelToPanel(projectInfoPanel, "<html><h1>" + selectedProjectName + "</h1></html>");
            JButton viewConversationsButton = createButton(localizationManager.getText("project.button.view.conversations"),
                    () -> {
                        try {
                            projectController.navigateToConversations(selectedProjectName);
                        } catch (RuntimeException e) {
                            showError(e.getMessage());
                        }
                    });
            addComponentWithSpacing(projectInfoPanel, viewConversationsButton);
        } else {
            addLabelToPanel(projectInfoPanel, "<html><h2>" + localizationManager.getText(PROJECT_NO_SELECTED) + "</h2></html>");
        }
        projectInfoPanel.revalidate();
        projectInfoPanel.repaint();
    }

    private void addProjectItem(String projectName) {
        JPanel projectItem = new JPanel();
        projectItem.setLayout(new BorderLayout());
        projectItem.setBackground(new Color(42, 46, 54));
        projectItem.setMaximumSize(new Dimension(300, 80));
        projectItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top panel (Project name + Select button)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(42, 46, 54));

        JLabel titleLabel = new JLabel("<html><b>" + projectName + "</b></html>");
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton selectButton = createButton(localizationManager.getText("generic.select"), () -> {
            selectedProjectName = projectName;
            updateProjectInfoPanel();
            updateButtonState();
        });
        topPanel.add(selectButton, BorderLayout.EAST);

        // Bottom panel (Add User + Remove User buttons)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(42, 46, 54));

        JButton addUserButton = createButton(localizationManager.getText("project.button.add.user"), this::addUserToProject);
        JButton removeUserButton = createButton(localizationManager.getText("project.button.delete.user"), this::removeUserFromProject);

        // Initially disable buttons until project is selected
        addUserButton.setEnabled(false);
        removeUserButton.setEnabled(false);

        addUserButtons.put(projectName, addUserButton);
        removeUserButtons.put(projectName, removeUserButton);

        bottomPanel.add(addUserButton);
        bottomPanel.add(removeUserButton);

        // Add panels to the main container
        projectItem.add(topPanel, BorderLayout.NORTH);
        projectItem.add(bottomPanel, BorderLayout.SOUTH);

        // Add projectItem to the main list
        projectListPanel.add(projectItem);
        projectListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void updateButtonState() {
        addUserButtons.forEach((project, button) -> button.setEnabled(project.equals(selectedProjectName)));
        removeUserButtons.forEach((project, button) -> button.setEnabled(project.equals(selectedProjectName)));
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(_ -> action.run());
        return button;
    }

    @Override
    public String showRemoveUserFromProjectDialog(List<String> usernames) {
        JComboBox<String> userDropdown = new JComboBox<>(usernames.toArray(new String[0]));

        JPanel panel = new JPanel();
        panel.add(new JLabel(localizationManager.getText("admin.remove.user.prompt")));
        panel.add(userDropdown);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                localizationManager.getText("project.remove.user.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return (String) userDropdown.getSelectedItem();
        }
        return null;
    }

    private String showUserSelectionDialog(List<String> users) {
        JComboBox<String> userDropdown = new JComboBox<>(users.toArray(new String[0]));

        JPanel panel = new JPanel();
        panel.add(new JLabel(localizationManager.getText("project.add.user.prompt.username")));
        panel.add(userDropdown);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                localizationManager.getText("project.add.user.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return (result == JOptionPane.OK_OPTION) ? (String) userDropdown.getSelectedItem() : null;
    }


    @Override
    public void navigateToProjectDetails(String projectName, String projectDescription) {
        JOptionPane.showMessageDialog(this, localizationManager.getText("project.details.view") + ": " + projectName, localizationManager.getText("view.title"), JOptionPane.INFORMATION_MESSAGE);
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
    public String getInput(String promptKey) {
        return JOptionPane.showInputDialog(this, localizationManager.getText(promptKey));
    }

    @Override
    public void close() {
        dispose();
    }

    private void addLabelToPanel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void addComponentWithSpacing(JPanel panel, JComponent component) {
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    @Override
    public void addUserToProject() {
        if (selectedProjectName == null) {
            showError(localizationManager.getText(PROJECT_NO_SELECTED));
            return;
        }

        try {
            // Retrieve users not already in the project
            List<UserBean> availableUsers = projectController.getUsersNotInProject(selectedProjectName);
            List<String> availableUsernames = availableUsers.stream()
                    .map(UserBean::getUsername)
                    .toList();

            if (availableUsers.isEmpty()) {
                showError(localizationManager.getText("project.add.user.error.no.available"));
                return;
            }

            // Show dropdown menu
            String selectedUsername = showUserSelectionDialog(availableUsernames);

            if (selectedUsername != null) {
                projectController.addUserToProject(selectedProjectName, selectedUsername);
                showSuccess(localizationManager.getText("project.add.user.success"));
            }

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }


    @Override
    public void removeUserFromProject() {
        if (selectedProjectName == null) {
            showError(localizationManager.getText(PROJECT_NO_SELECTED));
            return;
        }

        try {
            List<String> usernames = projectController.getUsersInProject(selectedProjectName)
                    .stream()
                    .map(UserBean::getUsername)
                    .toList();

            if (usernames.isEmpty()) {
                showError(localizationManager.getText("project.remove.user.empty"));
                return;
            }
            String selectedUsername = showRemoveUserFromProjectDialog(usernames);

            if (selectedUsername != null) {
                projectController.removeUserFromProject(selectedProjectName, selectedUsername);
                showSuccess(localizationManager.getText("project.remove.user.success"));
            }

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    @Override
    public void refresh() {
        displayProjects();
    }

    @Override
    public void back() {
        applicationController.back();
    }

    @Override
    public boolean isGraphic() {
        return true;
    }
}
