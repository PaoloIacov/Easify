package view.ProjectView.DecoratedProjectView;

import controller.ActionHandler;
import model.bean.ProjectBean;
import view.ProjectView.ProjectView;

import java.util.List;

public abstract class ProjectViewDecorator implements ProjectView {
    protected final ProjectView decoratedProjectView;

    protected ProjectViewDecorator(ProjectView projectView) {
        this.decoratedProjectView = projectView;
    }

    @Override
    public void displayProjects(List<ProjectBean> projects) {
        decoratedProjectView.displayProjects(projects);
    }

    @Override
    public void navigateToProjectDetails(String projectName, String projectDescription) {
        decoratedProjectView.navigateToProjectDetails(projectName, projectDescription);
    }

    @Override
    public String showRemoveUserFromProjectDialog(List<String> usernames) {
        return decoratedProjectView.showRemoveUserFromProjectDialog(usernames);
    }

    @Override
    public String getInput(String promptKey) {
        return decoratedProjectView.getInput(promptKey);
    }

    @Override
    public boolean isGraphic() {
        return decoratedProjectView.isGraphic();
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        decoratedProjectView.setActionHandler(handler);
    }

    @Override
    public void display() {
        decoratedProjectView.display();
    }

    @Override
    public void refresh() {
        decoratedProjectView.refresh();
    }

    @Override
    public void back() {
        decoratedProjectView.back();
    }

    @Override
    public void close() {
        decoratedProjectView.close();
    }

    @Override
    public void showSuccess(String message) {
        decoratedProjectView.showSuccess(message);
    }

    @Override
    public void showError(String message) {
        decoratedProjectView.showError(message);
    }

    @Override
    public void addUserToProject(String projectName, String username) {
        decoratedProjectView.addUserToProject(projectName, username);
    }

    @Override
    public String getSelectedProjectName() {
        return decoratedProjectView.getSelectedProjectName();
    }

    @Override
    public String getSelectedProjectDescription() {
        return decoratedProjectView.getSelectedProjectDescription();
    }

    @Override
    public void removeUserFromProject(String projectName, String username) {
        decoratedProjectView.removeUserFromProject(projectName, username);
    }
}
