package view.ProjectView.DecoratedProjectView;

import model.localization.LocalizationManager;
import view.ProjectView.ProjectView;

import java.util.List;

public class CliAdminProjectViewDecorator extends ProjectViewDecorator {
    private final LocalizationManager localizationManager;

    public CliAdminProjectViewDecorator(ProjectView projectView, LocalizationManager localizationManager) {
        super(projectView);
        this.localizationManager = localizationManager;
    }

    @Override
    public void display() {
        displayAdminMenu();
    }

    private void displayAdminMenu() {
        System.out.println("\n=== " + localizationManager.getText("admin.menu.title") + " ===");
        System.out.println(localizationManager.getText("project.menu.admin.options"));
    }

    public String removeProject(List<String> projectNames) {
        if (projectNames == null || projectNames.isEmpty()) {
            System.out.println(localizationManager.getText("project.delete.no.projects"));
            return null;
        }
        String projectName = getInput("project.delete.prompt.name");

        if (!projectName.isEmpty()) {
            showSuccess(localizationManager.getText("project.delete.success"));
        } else {
            showError(localizationManager.getText("project.delete.error"));
        }

        return projectName;
    }
}
