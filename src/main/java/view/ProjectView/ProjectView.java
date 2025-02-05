package view.ProjectView;

import controller.ActionHandler;
import model.bean.ProjectBean;
import view.View;

import java.util.List;

public interface ProjectView extends View {
    void displayProjects(List<ProjectBean> projects);
    void navigateToProjectDetails(String projectName, String projectDescription);
    void addUserToProject(String projectName, String username);
    void removeUserFromProject(String projectName, String username);
    void showSuccess(String message);
    void showError(String message);
    String getSelectedProjectName();
    String getSelectedProjectDescription();
    String showRemoveUserFromProjectDialog(List<String> usernames);
    void setActionHandler(ActionHandler handler);
}
