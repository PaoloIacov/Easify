package controller;

import model.bean.CredentialsBean;
import model.bean.ProjectBean;
import model.bean.UserBean;
import model.converter.ProjectConverter;
import model.converter.UserConverter;
import model.dao.ProjectDAO;
import model.dao.UserDAO;
import model.localization.LocalizationManager;

import java.sql.SQLException;
import java.util.List;

public class ProjectController {
    private final ProjectDAO projectDAO;
    private final LocalizationManager localizationManager;
    private final ApplicationController applicationController;
    private final CredentialsBean loggedInUser;
    private final UserDAO userDAO;
    private static final String GENERIC_ERROR  = "generic.error";

    public ProjectController(ProjectDAO projectDAO, LocalizationManager localizationManager, ApplicationController applicationController, CredentialsBean loggedInUser, UserDAO userDAO) {
        this.projectDAO = projectDAO;
        this.localizationManager = localizationManager;
        this.applicationController = applicationController;
        this.loggedInUser = loggedInUser;
        this.userDAO = userDAO;
    }

    public List<ProjectBean> getProjectsForUser(String username) {
        try {
            return projectDAO.getProjectsForUser(username).stream()
                    .map(ProjectConverter::toBean)
                    .toList();
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
        return List.of();
    }

    public void addUserToProject(String projectName, String username) {
        try {
            if (userDAO.getUserRole(username) == -1) {
                throw new IllegalArgumentException(localizationManager.getText("project.add.user.error.not.found"));
            }

            if (userDAO.isUserInProject(projectName, username)) {
                throw new IllegalArgumentException(localizationManager.getText("project.add.user.error.already.in.project"));
            }

            projectDAO.addEmployeeToProject(projectName, username);
        } catch (SQLException e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
    }

    public void removeUserFromProject(String projectName, String username) {
        try {
            projectDAO.removeEmployeeFromProject(projectName, username);
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
    }

    public void addProject(String projectName, String description) {
        try {
            validateProjectFields(projectName, description);
            projectDAO.addProject(projectName, description);
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
    }

    public void removeProject(String projectName) {
        try {
            projectDAO.deleteProject(projectName);
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
    }

    public void navigateToConversations(String projectName) {
        try {
            applicationController.navigateToConversation(loggedInUser, projectName);
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
    }

    public void validateProjectFields(String projectName, String description) {
        if (projectName == null || projectName.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException(localizationManager.getText("project.not.null"));
        }
    }

    public List<UserBean> getUsersInProject(String projectName) {
        try {
            return projectDAO.getUsersFromProject(projectName).stream()
                    .map(UserConverter::toBean)
                    .toList();
        } catch (Exception e) {
            System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
        return List.of();
    }

    public List<UserBean> getUsersNotInProject(String projectName) {
        try {
            return projectDAO.getUsersNotInProject(projectName).stream()
                    .map(UserConverter::toBean)
                    .toList();
        } catch (Exception e) {
           System.err.println(localizationManager.getText(GENERIC_ERROR) + ": " + e.getMessage());
        }
        return List.of();
    }
}
