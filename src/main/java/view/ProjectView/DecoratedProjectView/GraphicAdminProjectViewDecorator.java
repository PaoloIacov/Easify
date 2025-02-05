package view.ProjectView.DecoratedProjectView;

import controller.ActionHandler;
import model.localization.LocalizationManager;
import view.ProjectView.GraphicProjectView;
import view.ProjectView.ProjectView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GraphicAdminProjectViewDecorator extends ProjectViewDecorator {
    private ActionHandler actionHandler;
    private final LocalizationManager localizationManager;

    public GraphicAdminProjectViewDecorator(ProjectView projectView, LocalizationManager localizationManager) {
        super(projectView);
        this.localizationManager = localizationManager;
        replaceHeaderWithAdminHeader();
    }

    private JButton createButton(String text, String actionCommand) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(140, 30));
        button.addActionListener(_ -> {
            if (actionHandler != null) {
                actionHandler.handleAction(actionCommand);
            }
        });
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
        JPanel adminHeader = new JPanel(new GridBagLayout());
        adminHeader.setBackground(Color.WHITE);
        adminHeader.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JButton addProjectButton = createButton(localizationManager.getText("admin.option.add.project"), "5");
        gbc.gridx = 0;
        adminHeader.add(addProjectButton, gbc);

        JButton deleteProjectButton = createButton(localizationManager.getText("admin.option.delete.project"), "6");
        gbc.gridx = 1;
        adminHeader.add(deleteProjectButton, gbc);

        JButton backButton = createButton(localizationManager.getText("generic.back"), "4");
        gbc.gridx = 2;
        adminHeader.add(backButton, gbc);

        return adminHeader;
    }

    /**
     * **Dropdown menu per eliminare un progetto**
     */
    public String removeProject(List<String> projectNames) {
        if (projectNames.isEmpty()) {
            showError(localizationManager.getText("admin.remove.no.projects"));
            return null;
        }

        JComboBox<String> projectDropdown = new JComboBox<>(projectNames.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(localizationManager.getText("admin.remove.project.prompt")));
        panel.add(projectDropdown);

        int result = JOptionPane.showConfirmDialog(
                null, panel, localizationManager.getText("admin.remove.project.title"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return (String) projectDropdown.getSelectedItem();
        }

        return null;
    }

    @Override
    public boolean isGraphic() {
        return true;
    }

    @Override
    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        decoratedProjectView.setActionHandler(actionHandler);
    }
}
