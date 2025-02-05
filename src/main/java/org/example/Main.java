package org.example;

import controller.ApplicationController;
import model.localization.LocalizationManager;
import model.dao.ConnectionFactory;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = ConnectionFactory.getConnection();
            LocalizationManager localizationManager = new LocalizationManager();
            ApplicationController applicationController = new ApplicationController(connection, localizationManager);

            applicationController.start();
        } catch (Exception e) {
            System.err.println("Error starting the application." + e.getMessage());
        }
    }
}
