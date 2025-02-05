package controller;

import controller.exceptions.NullFieldException;
import model.bean.CredentialsBean;
import model.bean.ProjectBean;
import model.bean.UserBean;
import model.converter.ProjectConverter;
import model.converter.UserConverter;
import model.dao.ProjectDAO;
import model.dao.UserDAO;
import model.domain.Project;
import model.domain.User;
import model.localization.LocalizationManager;
import view.ProjectView.DecoratedProjectView.CliAdminProjectViewDecorator;
import view.ProjectView.DecoratedProjectView.GraphicAdminProjectViewDecorator;
import view.ProjectView.ProjectView;

import java.sql.SQLException;
import java.util.List;

public class ProjectController implements ActionHandler {

    private final ProjectView projectView;
    private final ProjectDAO projectDAO;
    private final LocalizationManager localizationManager;
    private final ApplicationController applicationController;
    private final CredentialsBean loggedInUser;
    private final UserDAO userDAO;
    private static final String GENERIC_ERROR = "error.generic";
    private static final String INVALID_OPTION = "error.invalid.option";

    public ProjectController(ProjectView projectView, ProjectDAO projectDAO, LocalizationManager localizationManager, ApplicationController applicationController, CredentialsBean loggedInUser, UserDAO userDAO) {
        this.projectView = projectView;
        this.projectDAO = projectDAO;
        this.localizationManager = localizationManager;
        this.applicationController = applicationController;
        this.loggedInUser = loggedInUser;
        this.userDAO = userDAO;

        if (projectView.isGraphic()) {
            projectView.setActionHandler(this);
        }
    }

    public void start(String username) {
        if (!projectView.isGraphic()) {
            runCliLoop();
        } else {
            displayProjectsForUser(username);
            projectView.display();

        }
    }

    @Override
    public boolean handleAction(String action) {
        try {
            switch (action) {
                case "1":
                    displayProjectsForUser(loggedInUser.getUsername());
                    break;
                case "2":
                    handleAddUserToProject();
                    break;
                case "3":
                    handleRemoveUserFromProject();
                    break;
                case "4":
                    applicationController.back();
                    return false;
                case "5": //Admin only
                    handleAddProject();
                    displayProjectsForUser(loggedInUser.getUsername());
                    break;
                case "6": //Admin only
                    handleRemoveProject();
                    break;
                case "8":
                    navigateToConversations();
                    break;
                case "7":
                    String projectNameForDetails = projectView.getSelectedProjectName();
                    String projectDescriptionForDetails = projectView.getSelectedProjectDescription();
                    if (projectNameForDetails != null) {
                        handleNavigateToProjectDetails(projectNameForDetails, projectDescriptionForDetails);
                    }
                    break;
                default:
                    projectView.showError(localizationManager.getText(INVALID_OPTION));
            }
        } catch (Exception e) {
            projectView.showError(GENERIC_ERROR);
        }
        return true;
    }


    private void runCliLoop() {
        while (true) {
            try {
                displayProjectsForUser(loggedInUser.getUsername());
                projectView.display();
                String action = projectView.getInput("project.menu.prompt").trim();
                int choice = Integer.parseInt(action);
                if (choice < 1 || choice > 5 && loggedInUser.getRole() != 3) {
                    projectView.showError(INVALID_OPTION);
                    continue;
                }
                switch (action) {
                    case "4" -> handleAction("8");
                    case "5" -> handleAction("4");
                    case "6" -> handleAction("5");
                    case "7" -> handleAction("6");
                    default -> handleAction(action);
                }

            } catch (NumberFormatException e) {
                projectView.showError(localizationManager.getText(INVALID_OPTION));
            } catch (Exception e) {
                projectView.showError(GENERIC_ERROR);
            }
        }
    }


    private void displayProjectsForUser(String username) {
        try {
            List<ProjectBean> projects = projectDAO.getProjectsForUser(username).stream()
                    .map(ProjectConverter::toBean)
                    .toList();
            projectView.displayProjects(projects);
        } catch (Exception e) {
            projectView.showError(localizationManager.getText("error.load.projects") + ": " + e.getMessage());
        }
    }

    private void handleNavigateToProjectDetails(String projectName, String projectDescription) {
        projectView.navigateToProjectDetails(projectName, projectDescription);
    }

    private void handleAddUserToProject() {
        try {
            displayProjectsForUser(loggedInUser.getUsername());
            String projectName = projectView.getSelectedProjectName();
            List<User> users = userDAO.getAllUsers();
            List<String> usernames = users.stream()
                    .map(User::getUsername)
                    .toList();

            if (projectName == null) {
                projectView.showError(localizationManager.getText("project.no.selected"));
                return;
            }
            System.out.println("Usernames: " + usernames);
            String username = projectView.getInput("project.add.user.prompt.username").trim();

            int userRole = userDAO.getUserRole(username);
            if (userRole == -1) {
                projectView.showError(localizationManager.getText("project.add.user.error.not.found"));
                return;
            }

            if (userDAO.isUserInProject(projectName, username)) {
                projectView.showError(localizationManager.getText("project.add.user.error.already.in.project"));
                return;
            }
            projectDAO.addEmployeeToProject(projectName, username);
            projectView.showSuccess(localizationManager.getText("project.add.user.success"));

        } catch (SQLException e) {
            projectView.showError("user.not.found");
        } catch (Exception e) {
            projectView.showError(GENERIC_ERROR);
        }
    }


    private void handleRemoveUserFromProject() {
        try {
            String projectName = projectView.getSelectedProjectName();
            if (projectName == null) {
                projectView.showError(localizationManager.getText("project.no.selected"));
                return;
            }
            List<User> users = projectDAO.getUsersFromProject(projectName);
            List<UserBean> userBeans = users.stream()
                    .map(UserConverter::toBean)
                    .toList();
            List<String> usernames = userBeans.stream()
                    .map(UserBean::getUsername)
                    .toList();
            if (usernames.isEmpty()) {
                projectView.showError(localizationManager.getText("project.remove.user.empty"));
                return;
            }

            String selectedUsername = projectView.showRemoveUserFromProjectDialog(usernames);
            if (selectedUsername == null) {
                return;
            }
            projectDAO.removeEmployeeFromProject(projectName, selectedUsername);
            projectView.showSuccess(localizationManager.getText("project.remove.user.success"));
        } catch (Exception e) {
            projectView.showError(localizationManager.getText("project.remove.user.error") + ": " + e.getMessage());
        }
    }

    private void handleAddProject() {
        try {
            String projectName = projectView.getInput("project.name.prompt");
            String description = projectView.getInput("project.add.prompt.description");
            validateProjectFields(projectName, description);
            projectDAO.addProject(projectName, description);
            projectView.showSuccess(localizationManager.getText("project.add.success"));
        } catch (Exception e) {
            projectView.showError(localizationManager.getText("project.add.error") + ": " + e.getMessage());
        }
    }

    private void handleRemoveProject() {
        try {
            List<Project> projects = projectDAO.getAllProjects();

            List<ProjectBean> projectBeans = projects.stream()
                    .map(ProjectConverter::toBean)
                    .toList();

            //Obtain the names for the dropdown menu
            List<String> projectNames = projectBeans.stream()
                    .map(ProjectBean::getName)
                    .toList();

            String selectedProjectName;
            switch (projectView) {
                case GraphicAdminProjectViewDecorator adminView ->
                    selectedProjectName = adminView.removeProject(projectNames);

                case CliAdminProjectViewDecorator adminView -> selectedProjectName = adminView.removeProject(projectNames);
                default -> {
                    projectView.showError(localizationManager.getText("admin.remove.project.not.allowed"));
                    return;
                }
            }
            if (selectedProjectName == null) {
                return;
            }

            ProjectBean projectToDelete = projectBeans.stream()
                    .filter(p -> p.getName().equals(selectedProjectName))
                    .findFirst()
                    .orElse(null);

            if (projectToDelete == null) {
                projectView.showError(localizationManager.getText("admin.remove.project.error"));
                return;
            }

            projectDAO.deleteProject(projectToDelete.getName());

            projectView.showSuccess(localizationManager.getText("admin.remove.project.success"));
            displayProjectsForUser(loggedInUser.getUsername());
        } catch (Exception e) {
            projectView.showError(localizationManager.getText("admin.remove.project.error") + ": " + e.getMessage());
        }
    }

    private void navigateToConversations() {
       try {

            applicationController.navigateToConversation(loggedInUser, projectView.getSelectedProjectName());
        } catch (Exception e) {
            projectView.showError("Failed to navigate to project view: " + e.getMessage());
        }
    }

    private void validateProjectFields(String projectName, String description) throws NullFieldException {
        if (projectName == null || projectName.isBlank() || description == null || description.isBlank()) {
            throw new NullFieldException("project.not.null", localizationManager);
        }
    }
}

