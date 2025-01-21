package controller;

import controller.exceptions.LoginExceptions.EmptyCredentialsException;
import controller.exceptions.LoginExceptions.InvalidCredentialsException;
import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import model.dao.LoginDAO;
import model.domain.Credentials;
import view.LoginView.GraphicLoginView;
import view.LoginView.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {

    private final LoginView loginView;
    private final LoginDAO loginDAO;

    public LoginController(LoginView loginView, LoginDAO loginDAO) {
        this.loginView = loginView;
        this.loginDAO = loginDAO;

        // Register the submit button listener if the view is a GraphicLoginView
        if (loginView instanceof GraphicLoginView graphicLoginView) {
            graphicLoginView.setSubmitButtonListener(new SubmitButtonListener());
        }
    }

    public void start() {
        loginView.display();
    }

    //Submit listener
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Catches credentials from the View
                CredentialsBean credentialsBean = loginView.getCredentialsInput();

                Credentials credentials = new Credentials();
                credentials.setUsername(credentialsBean.getUsername());
                credentials.setPassword(credentialsBean.getPassword());

                if (credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty()) {
                   throw new EmptyCredentialsException();
                }

                boolean isValid = loginDAO.validateCredentials(credentials);

                if (isValid) {
                    loginView.showSuccess("login.success");
                } else {
                    throw new InvalidCredentialsException();
                }
            } catch (LoginException ex) {
               loginView.showError(ex.getMessageKey());
            } catch (Exception ex) {
                loginView.showError("error.generic");
            }
        }
    }
}
