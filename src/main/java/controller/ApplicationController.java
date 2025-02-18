package controller;

import model.bean.CredentialsBean;
import model.dao.*;
import model.dao.LoginDAO.DbmsLoginDAO;
import model.dao.MessageDAO;
import model.dao.UserDAO;
import model.localization.LocalizationManager;
import view.AdminView.*;
import view.ConversationView.*;
import view.ConversationView.DecoratedConversationView.*;
import view.LoginView.*;
import view.ProjectView.*;
import view.ProjectView.DecoratedProjectView.*;
import view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;

public class ApplicationController {

    private final Connection connection;
    private final LocalizationManager localizationManager;
    private final Stack<View> viewStack = new Stack<>();
    private String currentInterface = "CLI";
    private static final String GRAPHIC = "Graphic";
    public ApplicationController(Connection connection, LocalizationManager localizationManager) {
        this.connection = connection;
        this.localizationManager = localizationManager;
    }

    public void start() {
        chooseInterface();
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
        LoginView loginView;
        LoginController loginController = new LoginController(
                new DbmsLoginDAO(connection),
                new UserDAO(connection),
                this
        );

        if (currentInterface.equals(GRAPHIC)) {
            loginView = new GraphicLoginView(localizationManager, loginController);
        } else {
            loginView = new CliLoginView(localizationManager, loginController);
        }
        loginView.display();
        viewStack.push(loginView);
    }

    public void handleRoleBasedNavigation(CredentialsBean loggedInUser) throws SQLException {
        if (loggedInUser == null) {
            System.out.println(localizationManager.getText("login.failed"));
            navigateToLogin();
            return;
        }

        switch (loggedInUser.getRole()) {
            case 1 -> navigateToConversation(loggedInUser, null);
            case 2 -> navigateToProject(loggedInUser);
            case 3 -> navigateToAdmin(loggedInUser);
            default -> {
                System.out.println(localizationManager.getText("role.unknownRole"));
                navigateToLogin();
            }
        }
    }

    public void navigateToAdmin(CredentialsBean loggedInUser) throws SQLException {
        AdminView adminView;
        AdminController adminController = new AdminController(connection, this, localizationManager, loggedInUser);

        if (currentInterface.equals(GRAPHIC)) {
            adminView = new GraphicAdminView(localizationManager, adminController);
        } else {
            adminView = new CliAdminView(localizationManager, adminController);
        }
        adminView.display();
        viewStack.push(adminView);
    }

    public void navigateToProject(CredentialsBean loggedInUser) {
        ProjectView projectView;

        ProjectController projectController = new ProjectController(
                new ProjectDAO(connection),
                localizationManager,
                this,
                loggedInUser,
                new UserDAO(connection)
        );

        if (currentInterface.equals(GRAPHIC)) {
            projectView = loggedInUser.getRole() == 3
                    ? new GraphicAdminProjectViewDecorator(new GraphicProjectView(localizationManager, projectController, loggedInUser, this), localizationManager, projectController, loggedInUser)
                    : new GraphicProjectView(localizationManager, projectController, loggedInUser, this);
        } else {
            projectView = loggedInUser.getRole() == 3
                    ? new CliAdminProjectViewDecorator(new CliProjectView(localizationManager, projectController, loggedInUser), localizationManager)
                    : new CliProjectView(localizationManager, projectController, loggedInUser);
        }

        projectView.display();
        viewStack.push(projectView);
        System.out.println("Using ProjectView: " + projectView.getClass().getSimpleName());
    }

    public void navigateToConversation(CredentialsBean loggedInUser, String projectName) {
        ConversationView conversationView;

        ConversationController conversationController = new ConversationController(
                new ConversationDAO(connection),
                new MessageDAO(connection),
                localizationManager,
                loggedInUser,
                this,
                projectName
        );

        if (currentInterface.equals(GRAPHIC)) {
            conversationView = projectName != null
                    ? new GraphicPmConversationViewDecorator(new GraphicConversationView(localizationManager, conversationController), localizationManager, conversationController)
                    : new GraphicConversationView(localizationManager, conversationController);
        } else {
            conversationView = projectName != null
                    ? new CliPmConversationViewDecorator(new CliConversationView(localizationManager, conversationController), localizationManager)
                    : new CliConversationView(localizationManager, conversationController);
        }



        viewStack.push(conversationView);
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
