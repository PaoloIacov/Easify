package controller;

import controller.exceptions.LoginExceptions.EmptyCredentialsException;
import controller.exceptions.LoginExceptions.InvalidCredentialsException;
import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import model.converter.CredentialsConverter;
import model.dao.LoginDAO.LoginDAO;
import model.dao.UserDAO;
import model.domain.Credentials;

import java.sql.SQLException;

public class LoginController {

    private final LoginDAO loginDAO;
    private final UserDAO userDAO;
    private final ApplicationController applicationController;

    public LoginController(LoginDAO loginDAO, UserDAO userDAO, ApplicationController applicationController) {
        this.loginDAO = loginDAO;
        this.userDAO = userDAO;
        this.applicationController = applicationController;
    }

    public CredentialsBean authenticate(CredentialsBean credentialsBean) throws LoginException, SQLException {
        if (credentialsBean == null || credentialsBean.getUsername().isEmpty() || credentialsBean.getPassword().isEmpty()) {
            throw new EmptyCredentialsException();
        }

        Credentials credentials = CredentialsConverter.toDomain(credentialsBean);

        boolean isValid = loginDAO.validateCredentials(credentials);
        if (!isValid) {
            throw new InvalidCredentialsException();
        }

        int role = userDAO.getUserRole(credentials.getUsername());
        credentialsBean.setRole(role);

        return credentialsBean;
    }


    public void navigateBasedOnRole(CredentialsBean credentialsBean) throws SQLException {
        applicationController.handleRoleBasedNavigation(credentialsBean);
    }
}
