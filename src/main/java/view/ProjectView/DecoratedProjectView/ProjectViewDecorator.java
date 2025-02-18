package view.ProjectView.DecoratedProjectView;

import view.ProjectView.ProjectView;

import java.util.List;

public abstract class ProjectViewDecorator implements ProjectView {
    protected final ProjectView decoratedProjectView;

    protected ProjectViewDecorator(ProjectView projectView) {
        this.decoratedProjectView = projectView;
    }

    @Override
    public void displayProjects() {
        decoratedProjectView.displayProjects();
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
    public void addUserToProject() {
        decoratedProjectView.addUserToProject();
    }


    @Override
    public void removeUserFromProject() {
        decoratedProjectView.removeUserFromProject();
    }
}
