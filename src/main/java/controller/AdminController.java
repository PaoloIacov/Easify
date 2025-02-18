package controller;

import controller.exceptions.NullFieldException;
import model.bean.CredentialsBean;
import model.bean.UserBean;
import model.converter.UserConverter;
import model.dao.UserDAO;
import model.domain.User;
import model.localization.LocalizationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private final UserDAO userDAO;
    private final ApplicationController applicationController;
    private final LocalizationManager localizationManager;
    private final CredentialsBean loggedInUser;

    public AdminController(Connection connection, ApplicationController applicationController, LocalizationManager localizationManager, CredentialsBean loggedInUser) {
        this.userDAO = new UserDAO(connection);
        this.applicationController = applicationController;
        this.localizationManager = localizationManager;
        this.loggedInUser = loggedInUser;
    }

    public List<UserBean> getAllUsers() throws SQLException {
        return userDAO.getAllUsers().stream().map(UserConverter::toBean).toList();
    }

    public void addUser(UserBean userBean) throws NullFieldException, SQLException {
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
    }

    public void removeUser(String username) throws SQLException {
        userDAO.deleteUser(username);
    }

    public UserBean getUserDetails(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        return UserConverter.toBean(user);
    }

    public void navigateToProjectView() {
        applicationController.navigateToProject(loggedInUser);
    }

    public void back() {
        applicationController.back();
    }

    private void validateUserFields(String username, String password, String name, String surname, int role) throws NullFieldException {
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
