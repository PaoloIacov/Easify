package controller;

import controller.exceptions.LoginExceptions.EmptyCredentialsException;
import controller.exceptions.LoginExceptions.InvalidCredentialsException;
import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import model.converter.CredentialsConverter;
import model.dao.LoginDAO.LoginDAO;
import model.dao.UserDAO;
import model.domain.Credentials;
import view.LoginView.CliLoginView;
import view.LoginView.GraphicLoginView;
import view.LoginView.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginController {

    private final LoginView loginView;
    private final LoginDAO loginDAO;
    private final UserDAO userDAO;
    private final ApplicationController applicationController;
    private static final String GENERIC_ERROR = "error.generic";

    public LoginController(LoginView loginView, LoginDAO loginDAO, UserDAO userDAO, ApplicationController applicationController) {
        this.loginView = loginView;
        this.loginDAO = loginDAO;
        this.userDAO = userDAO;
        this.applicationController = applicationController;


    }

    public void start() {
        if (loginView instanceof CliLoginView) {
            runCliLoop();
        } else if (loginView instanceof GraphicLoginView graphicLoginView) {
            loginView.display();
            graphicLoginView.setSubmitButtonListener(new SubmitButtonListener());
        }
    }


    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                handleLogin();
            } catch (Exception ex) {
                loginView.showError(GENERIC_ERROR);
            }
        }
    }

    private void runCliLoop() {
        boolean loggedIn = false;
        while (!loggedIn) {
            try {
                handleLogin();
                loggedIn = true;
            } catch (Exception e) {
                loginView.showError(GENERIC_ERROR);
            }
        }
    }


    public void handleLogin() {
        try {
            // Retrieve credentials from the view
            CredentialsBean credentialsBean = loginView.getCredentialsInput();
            Credentials credentials = CredentialsConverter.toDomain(credentialsBean);

            if (credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty()) {
                throw new EmptyCredentialsException();
            }

            // Validate the credentials
            boolean isValid = loginDAO.validateCredentials(credentials);

            if (!isValid) {
                throw new InvalidCredentialsException();
            }

            // Fetch the user's role from the database
            int role = userDAO.getUserRole(credentials.getUsername());
            credentialsBean.setRole(role);

            // Show success message
            loginView.showSuccess();
            if (loginView instanceof CliLoginView) {
                System.out.println("Welcome, " + credentials.getUsername());
            } else if (loginView instanceof GraphicLoginView graphicLoginView) {
                graphicLoginView.close();
            }

            // Pass control to the ApplicationController for role-based navigation
            applicationController.handleRoleBasedNavigation(credentialsBean);

        } catch (SQLException e) {
            loginView.showError("error.database");
        } catch (LoginException ex) {
            loginView.showError(ex.getMessageKey());
            runCliLoop();
        } catch (Exception ex) {
            loginView.showError(GENERIC_ERROR);
        }
    }
}
