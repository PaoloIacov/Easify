package org.example;

import controller.LoginController;
import model.dao.ConnectionFactory;
import model.dao.LoginDAO;
import model.localization.LocalizationManager;
import view.LoginView.CliLoginView;
import view.LoginView.GraphicLoginView;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            LoginDAO loginDAO = new LoginDAO(connection);
            LocalizationManager localizationManager = new LocalizationManager();
            CliLoginView cliLoginView = new CliLoginView(localizationManager);
            GraphicLoginView graphicLoginView = new GraphicLoginView(localizationManager);
            LoginController loginController = new LoginController(graphicLoginView, loginDAO);
            loginController.start();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error establishing connection to the database.");
        }
    }
}