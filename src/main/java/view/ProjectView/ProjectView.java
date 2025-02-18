package view.ProjectView;

import view.View;

import java.util.List;

public interface ProjectView extends View {
    void displayProjects();
    void navigateToProjectDetails(String projectName, String projectDescription);
    void addUserToProject();
    void removeUserFromProject();
    void showSuccess(String message);
    void showError(String message);
    String showRemoveUserFromProjectDialog(List<String> usernames);
}
