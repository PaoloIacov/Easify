package controller;

import model.bean.CredentialsBean;
import model.dao.*;
import model.dao.LoginDAO.DbmsLoginDAO;
import model.dao.MessageDAO;
import model.dao.UserDAO;
import model.localization.LocalizationManager;
import view.Factory.CliViewFactory;
import view.Factory.GraphicViewFactory;
import view.Factory.ViewFactory;
import view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;

public class ApplicationController {

    private final Connection connection;
    private final LocalizationManager localizationManager;
    private final Stack<View> viewStack = new Stack<>();
    private ViewFactory viewFactory;

    private static final String GRAPHIC = "Graphic";
    private String currentInterface = "CLI";

    public ApplicationController(Connection connection, LocalizationManager localizationManager) {
        this.connection = connection;
        this.localizationManager = localizationManager;
    }

    public void start() {
        chooseInterface();
        viewFactory = currentInterface.equals(GRAPHIC) ? new GraphicViewFactory(localizationManager) : new CliViewFactory(localizationManager);
        navigateToLogin();
    }

    private void chooseInterface() {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println(localizationManager.getText("interface.choose"));
            System.out.println("1) " + localizationManager.getText("interface.graphic"));
            System.out.println("2) " + localizationManager.getText("interface.cli"));
            System.out.print(localizationManager.getText("interface.prompt"));

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> { currentInterface = GRAPHIC; validChoice = true; }
                case "2" -> { currentInterface = "CLI"; validChoice = true; }
                default -> System.out.println(localizationManager.getText("interface.invalid"));
            }
        }
        System.out.println(localizationManager.getText("interface.selected") + ": " + currentInterface);
    }

    public void navigateToLogin() {
        LoginController loginController = new LoginController(new DbmsLoginDAO(connection), new UserDAO(connection), this);
        View loginView = viewFactory.createLoginView(loginController);
        viewStack.push(loginView);
        loginView.display();
    }

    public void handleRoleBasedNavigation(CredentialsBean loggedInUser) throws SQLException {
        switch (loggedInUser.getRole()) {
            case 1 -> navigateToConversation(loggedInUser, null);
            case 2 -> navigateToProject(loggedInUser);
            case 3 -> navigateToAdmin(loggedInUser);
            default -> System.out.println("Invalid role");
        }
    }

    public void navigateToAdmin(CredentialsBean loggedInUser) throws SQLException {
        AdminController adminController = new AdminController(connection, this, localizationManager, loggedInUser);
        View adminView = viewFactory.createAdminView(adminController);
        viewStack.push(adminView);
        adminView.display();
    }

    public void navigateToProject(CredentialsBean loggedInUser) {
        ProjectController projectController = new ProjectController(new ProjectDAO(connection), localizationManager, this, loggedInUser, new UserDAO(connection));
        View projectView = viewFactory.createProjectView(projectController, loggedInUser);
        viewStack.push(projectView);
        projectView.display();
    }

    public void navigateToConversation(CredentialsBean loggedInUser, String projectName) {
        ConversationController conversationController = new ConversationController(new ConversationDAO(connection), new MessageDAO(connection), localizationManager, loggedInUser, this, projectName);
        View conversationView = viewFactory.createConversationView(conversationController, projectName);
        viewStack.push(conversationView);
        conversationView.display();
    }

    public void back() {
        if (viewStack.size() > 1) {
            View currentView = viewStack.pop();
            currentView.close();
            View previousView = viewStack.peek();
            previousView.display();
        }
    }
}
