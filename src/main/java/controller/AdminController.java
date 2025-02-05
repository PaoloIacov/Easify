package controller;

import controller.exceptions.NullFieldException;
import model.bean.CredentialsBean;
import model.bean.UserBean;
import model.dao.UserDAO;
import model.domain.User;
import model.localization.LocalizationManager;
import view.AdminView.AdminView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController implements ActionHandler {

    private final AdminView adminView;
    private final UserDAO userDAO;
    private final ApplicationController applicationController;
    private final LocalizationManager localizationManager;
    private final CredentialsBean loggedInUser;

    public AdminController(AdminView adminView, Connection connection, ApplicationController applicationController, LocalizationManager localizationManager, CredentialsBean loggedInUser) {
        this.adminView = adminView;
        this.userDAO = new UserDAO(connection);
        this.applicationController = applicationController;
        this.localizationManager = localizationManager;
        this.loggedInUser = loggedInUser;

        if (adminView.isGraphic()) {
            adminView.setActionHandler(this);
        }
    }

    public void start() {
        if (!adminView.isGraphic()) {
            runCliLoop();
        } else {
            adminView.display();
            displayAllUsers();
        }
    }

    private void runCliLoop() {
        boolean running = true;
        while (running) {
            try {
                adminView.display();
                String choice = adminView.getInput("admin.menu.prompt");
                running = handleAction(choice);
            } catch (Exception e) {
                adminView.showError("An error occurred: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean handleAction(String action) {
        switch (action) {
            case "1":
                displayAllUsers();
                break;
            case "2":
                handleaddUser();
                break;
            case "3":
                handleRemoveUser();
                break;
            case "4":
                navigateToProjectView();
                break;
            case "5":
                applicationController.back();
                return false;
            case "6":
                handleViewUserDetails();
                break;
            default:
                adminView.showError("Invalid choice. Please try again.");
                return true;
        }
        return true;
    }

    private void displayAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            List<UserBean> userBeans = users.stream().map(this::convertToUserBean).collect(Collectors.toList());
            adminView.displayAllUsers(userBeans);
        } catch (Exception e) {
            adminView.showError("Failed to fetch users: " + e.getMessage());
        }
    }

    private void handleaddUser() {
        try {
            UserBean userBean = adminView.addUser();

            if (userBean == null) {
                throw new NullFieldException("user.not.null", localizationManager);
            }

            validateUserFields(userBean.getUsername(), userBean.getPassword(), userBean.getName(), userBean.getSurname(), userBean.getRole());

            userDAO.addUser(
                    userBean.getUsername(),
                    userBean.getPassword(),
                    userBean.getName(),
                    userBean.getSurname(),
                    userBean.getRole()
            );

            adminView.showSuccess(localizationManager.getText("generic.action.success"));
            displayAllUsers();
        } catch (NullFieldException e) {
            adminView.showError(e.getMessageKey());
        } catch (SQLException e) {
            adminView.showError(localizationManager.getText("generic.error"));
        } catch (Exception e) {
            adminView.showError(localizationManager.getText("admin.add.error"));
        }
    }


    private void handleRemoveUser() {
        try {
            List<String> usernames = userDAO.getAllUsers().stream()
                    .map(User::getUsername)
                    .toList();

            if (usernames.isEmpty()) {
                adminView.showError(localizationManager.getText("admin.remove.no.users"));
                return;
            }

            String selectedUsername = adminView.removeUser(usernames);
            if (selectedUsername != null) {
                userDAO.deleteUser(selectedUsername);
                adminView.showSuccess(localizationManager.getText("generic.action.success"));
                displayAllUsers();
            } else {
                adminView.showError(localizationManager.getText("generic.action.cancelled"));
            }
        } catch (Exception e) {
            adminView.showError(localizationManager.getText("admin.remove.error") + ": " + e.getMessage());
        }
    }


    private void navigateToProjectView() {
        try {
            applicationController.navigateToProject(loggedInUser);
        } catch (Exception e) {
            adminView.showError("Failed to navigate to project view: " + e.getMessage());
        }
    }

    private void handleViewUserDetails() {
        try {
            String username = adminView.getSelectedUsername();
            User user = userDAO.getUserByUsername(username);
            UserBean userBean = convertToUserBean(user);
            adminView.displayUserDetails(userBean);
        } catch (Exception e) {
            adminView.showError(localizationManager.getText("admin.view.user.error") + ": " + e.getMessage());
        }
    }


    private UserBean convertToUserBean(User user) {
        return new UserBean(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getRole()
        );
    }

    private void validateUserFields(String username, String password, String name, String surname, int role) throws NullFieldException {
        System.out.println("Validating fields");
        if (username == null || username.trim().isEmpty()) {
            throw new NullFieldException("field.username.not.null", localizationManager);
        }
        if (password == null || password.trim().isEmpty()) {
            throw new NullFieldException("field.password.not.null", localizationManager);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new NullFieldException("field.name.not.null", localizationManager);
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new NullFieldException("field.surname.not.null", localizationManager);
        }
        if (role <= 0 || role > 3) {
            throw new NullFieldException("field.role.not.null", localizationManager);
        }
    }

}
