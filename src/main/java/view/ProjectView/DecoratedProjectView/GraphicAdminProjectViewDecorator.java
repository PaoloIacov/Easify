package view.ProjectView.DecoratedProjectView;

import controller.ProjectController;
import model.bean.CredentialsBean;
import model.bean.ProjectBean;
import model.localization.LocalizationManager;
import view.PanelUtils;
import view.ProjectView.GraphicProjectView;
import view.ProjectView.ProjectView;

import javax.swing.*;

import java.awt.*;
import java.util.List;

public class GraphicAdminProjectViewDecorator extends ProjectViewDecorator {
    private final LocalizationManager localizationManager;
    private final ProjectController projectController;
    private final CredentialsBean loggedInUser;

    public GraphicAdminProjectViewDecorator(ProjectView projectView, LocalizationManager localizationManager, ProjectController projectController, CredentialsBean loggedInUser) {
        super(projectView);
        this.localizationManager = localizationManager;
        this.projectController = projectController;
        this.loggedInUser = loggedInUser;
        replaceHeaderWithAdminHeader();
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(e -> action.run());
        return button;
    }
    private void replaceHeaderWithAdminHeader() {
        if (decoratedProjectView instanceof GraphicProjectView graphicView) {
            JPanel rightPanel = graphicView.getRightPanel();

            if (rightPanel != null) {
                Component oldHeader = rightPanel.getComponent(0);
                if (oldHeader != null) {
                    rightPanel.remove(oldHeader);
                }

                JPanel adminHeader = createAdminProjectHeader();
                rightPanel.add(adminHeader, BorderLayout.NORTH);

                rightPanel.revalidate();
                rightPanel.repaint();
            }
        }
    }

    private JPanel createAdminProjectHeader() {
        JPanel adminHeader = PanelUtils.createHeaderPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JButton addProjectButton = createButton(localizationManager.getText("admin.option.add.project"), this::addProject);
        gbc.gridx = 0;
        adminHeader.add(addProjectButton, gbc);

        JButton deleteProjectButton = createButton(localizationManager.getText("admin.option.delete.project"), this::removeProject);
        gbc.gridx = 1;
        adminHeader.add(deleteProjectButton, gbc);

        JButton backButton = createButton(localizationManager.getText("generic.back"), this::back);
        gbc.gridx = 2;
        adminHeader.add(backButton, gbc);

        return adminHeader;
    }

    public void addProject() {
        try {
            String projectName = JOptionPane.showInputDialog(localizationManager.getText("project.add.prompt.name"));
            String description = JOptionPane.showInputDialog(localizationManager.getText("project.add.prompt.description"));
            projectController.addProject(projectName, description);
        } catch (Exception e) {
            showError(localizationManager.getText("project.not.null"));
        }
        displayProjects();
    }

    public String showRemoveProjectDialog(List<String> projectNames) {
        if (projectNames.isEmpty()) {
            showError(localizationManager.getText("admin.remove.no.projects"));
            return null;
        }

        JComboBox<String> projectDropdown = new JComboBox<>(projectNames.toArray(new String[0]));

        JPanel panel = new JPanel();
        panel.add(new JLabel(localizationManager.getText("admin.remove.project.prompt")));
        panel.add(projectDropdown);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                localizationManager.getText("admin.remove.project.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return (String) projectDropdown.getSelectedItem();
        }
        return null;
    }

    public void removeProject() {
        try {
            List<String> projectNames = projectController.getProjectsForUser(loggedInUser.getUsername())
                    .stream()
                    .map(ProjectBean::getName)
                    .toList();

            if (projectNames.isEmpty()) {
                showError(localizationManager.getText("admin.remove.no.projects"));
                return;
            }

            String selectedProject = showRemoveProjectDialog(projectNames);

            if (selectedProject != null) {
                projectController.removeProject(selectedProject);
                showSuccess(localizationManager.getText("generic.action.success"));
                displayProjects();
            }
        } catch (RuntimeException e) {
            showError(localizationManager.getText("admin.remove.project.error") + ": " + e.getMessage());
        }
    }


    @Override
    public boolean isGraphic() {
        return true;
    }
}
