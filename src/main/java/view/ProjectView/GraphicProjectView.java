package view.ProjectView;

import controller.ActionHandler;
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
    private transient ActionHandler actionHandler;
    private JPanel projectListPanel;
    private BackgroundPanel projectInfoPanel;
    private String selectedProjectName;
    private String selectedProjectDescription;
    private JPanel rightPanel;
    private final Map<String, JButton> addEmployeeButtons = new HashMap<>();
    private final Map<String, JButton> deleteEmployeeButtons = new HashMap<>();
    private static final String SUCCESS = "success.title";

    public GraphicProjectView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
        initializeUI();
    }

    private void initializeUI() {
        setTitle(localizationManager.getText("project.view.title"));
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel leftPanel = createLeftPanel();
        rightPanel = createRightPanel();

        PanelUtils.setupMainLayout(this, leftPanel, rightPanel);
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

        JButton backButton = createButton(localizationManager.getText("generic.back"), "4");
        PanelUtils.addButtonToPanel(projectHeader, backButton, 0);

        return projectHeader;
    }


    private void updateProjectInfoPanel() {
        projectInfoPanel.removeAll();

        if (selectedProjectName != null && !selectedProjectName.trim().isEmpty()) {
            addLabelToPanel(projectInfoPanel, "<html><h1>" + localizationManager.getText("project.name") + ": " + selectedProjectName + "</h1></html>");
            addLabelToPanel(projectInfoPanel, "<html><p style='width: 300px;'>" + localizationManager.getText("project.description") + ": " + selectedProjectDescription + "</p></html>");

            JButton viewConversationsButton = createButton(localizationManager.getText("project.button.view.conversations"), "8");
            addComponentWithSpacing(projectInfoPanel, viewConversationsButton);
        } else {
            addLabelToPanel(projectInfoPanel, "<html><h2>" + localizationManager.getText("project.no.selected") + "</h2></html>");
        }

        projectInfoPanel.revalidate();
        projectInfoPanel.repaint();
    }

    @Override
    public void displayProjects(List<ProjectBean> projects) {
        projectListPanel.removeAll();
        for (ProjectBean project : projects) {
            addProjectItem(project.getName(), project.getDescription());
        }
        projectListPanel.revalidate();
        projectListPanel.repaint();
    }

    private void addProjectItem(String projectName, String projectDescription) {
        JPanel projectItem = new JPanel();
        projectItem.setLayout(new BoxLayout(projectItem, BoxLayout.Y_AXIS));
        projectItem.setBackground(new Color(42, 46, 54));
        projectItem.setMaximumSize(new Dimension(300, 80));
        projectItem.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(42, 46, 54));

        JLabel titleLabel = new JLabel("<html><b>" + projectName + "</b></html>");
        titleLabel.setForeground(Color.WHITE);

        JButton selectButton = new JButton(localizationManager.getText("generic.select"));
        JButton addEmployeeButton = new JButton(localizationManager.getText("project.button.add.user"));
        JButton deleteEmployeeButton = new JButton(localizationManager.getText("project.button.delete.user"));

        addEmployeeButtons.put(projectName, addEmployeeButton);
        deleteEmployeeButtons.put(projectName, deleteEmployeeButton);

        addEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);

        selectButton.addActionListener(_ -> {
            selectedProjectName = projectName;
            selectedProjectDescription = projectDescription;
            updateProjectInfoPanel();

            // Disabilita tutti i bottoni
            addEmployeeButtons.values().forEach(button -> button.setEnabled(false));
            deleteEmployeeButtons.values().forEach(button -> button.setEnabled(false));

            addEmployeeButton.setEnabled(true);
            deleteEmployeeButton.setEnabled(true);
        });

        addEmployeeButton.addActionListener(_ -> {
            if (actionHandler != null) {
                actionHandler.handleAction("2");
            }
        });

        deleteEmployeeButton.addActionListener(_ -> {
            if (actionHandler != null) {
                actionHandler.handleAction("3");
            }
        });

        topPanel.add(titleLabel);
        topPanel.add(selectButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(42, 46, 54));
        bottomPanel.add(addEmployeeButton);
        bottomPanel.add(deleteEmployeeButton);

        projectItem.add(topPanel);
        projectItem.add(bottomPanel);

        projectListPanel.add(projectItem);
        projectListPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        projectListPanel.revalidate();
        projectListPanel.repaint();
    }

    @Override
   public void navigateToProjectDetails(String projectName, String projectDescription) {
        JOptionPane.showMessageDialog(this, localizationManager.getText("project.details.view") + ": " + projectName, localizationManager.getText("view.title"), JOptionPane.INFORMATION_MESSAGE);

   }

    @Override
    public void addUserToProject(String projectName, String username) {
        JOptionPane.showMessageDialog(this, localizationManager.getText("project.user.added") + ": " + username + " -> " + projectName, localizationManager.getText(SUCCESS), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void removeUserFromProject(String projectName, String username) {
        JOptionPane.showMessageDialog(this, localizationManager.getText("project.user.removed") + ": " + username + " -> " + projectName, localizationManager.getText(SUCCESS), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, localizationManager.getText(SUCCESS), JOptionPane.INFORMATION_MESSAGE);
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
    public boolean isGraphic() {
        return true;
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
    public String getSelectedProjectName() {
        if (selectedProjectName == null || selectedProjectName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    localizationManager.getText("project.no.selected"),
                    localizationManager.getText("error.title"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return selectedProjectName;
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


    @Override
    public String getSelectedProjectDescription() {
        if (selectedProjectDescription == null || selectedProjectDescription.isEmpty()) {
            return localizationManager.getText("project.no.description");
        }
        return selectedProjectDescription;
    }

    @Override
    public void back() {
        if (actionHandler != null) {
            actionHandler.handleAction("back");
        }
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        this.actionHandler = handler;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    private void addLabelToPanel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Aggiunge spazio tra gli elementi
    }

    private void addComponentWithSpacing(JPanel panel, JComponent component) {
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio dopo ogni componente
    }


}
