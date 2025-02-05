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
import java.util.Scanner;
import java.util.Stack;

public class ApplicationController {

    private final Connection connection;
    private final LocalizationManager localizationManager;
    private final Stack<View> viewStack = new Stack<>();
    private String currentInterface = "CLI";

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
            System.out.print(localizationManager.getText("interface.prompt") + ": ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> { currentInterface = "Graphic"; validChoice = true; }
                case "2" -> { currentInterface = "CLI"; validChoice = true; }
                default -> System.out.println(localizationManager.getText("interface.invalid"));
            }
        }
        System.out.println(localizationManager.getText("interface.selected") + ": " + currentInterface);
    }

    public void navigateToLogin() {
        LoginView loginView = createLoginView();
        viewStack.push(loginView);

        LoginController loginController = new LoginController(
                loginView,
                new DbmsLoginDAO(connection),
                new UserDAO(connection),
                this
        );
        loginController.start();
    }

    public void handleRoleBasedNavigation(CredentialsBean loggedInUser) {
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

    public void navigateToAdmin(CredentialsBean loggedInUser) {
        AdminView adminView = createAdminView();
        AdminController adminController = new AdminController(adminView, connection, this, localizationManager, loggedInUser);
        adminController.start();
        if (adminView.isGraphic()) {
            adminView.setActionHandler(adminController);
        }
        viewStack.push(adminView);
    }

    public void navigateToProject(CredentialsBean loggedInUser) {
        ProjectView projectView;

        if (currentInterface.equals("Graphic")) {
            projectView = loggedInUser.getRole() == 3
                    ? new GraphicAdminProjectViewDecorator(new GraphicProjectView(localizationManager), localizationManager)
                    : new GraphicProjectView(localizationManager);
        } else {
            projectView = loggedInUser.getRole() == 3
                    ? new CliAdminProjectViewDecorator(new CliProjectView(localizationManager), localizationManager)
                    : new CliProjectView(localizationManager);
        }

        ProjectController projectController = new ProjectController(
                projectView,
                new ProjectDAO(connection),
                localizationManager,
                this,
                loggedInUser,
                new UserDAO(connection)
        );

        projectController.start(loggedInUser.getUsername());
        viewStack.push(projectView);
        System.out.println("Using ProjectView: " + projectView.getClass().getSimpleName());
    }

    public void navigateToConversation(CredentialsBean loggedInUser, String projectName) {
        ConversationView conversationView;

        if (currentInterface.equals("Graphic")) {
            conversationView = projectName != null
                    ? new GraphicPmConversationViewDecorator(new GraphicConversationView(localizationManager), localizationManager)
                    : new GraphicConversationView(localizationManager);
        } else {
            conversationView = projectName != null
                    ? new CliPmConversationViewDecorator(new CliConversationView(localizationManager), localizationManager)
                    : new CliConversationView(localizationManager);
        }

        ConversationController conversationController = new ConversationController(
                conversationView,
                new ConversationDAO(connection),
                new MessageDAO(connection),
                localizationManager,
                loggedInUser,
                this
        );

        conversationController.start(projectName);
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

    private LoginView createLoginView() {
        return currentInterface.equals("Graphic")
                ? new GraphicLoginView(localizationManager)
                : new CliLoginView(localizationManager);
    }

    private AdminView createAdminView() {
        return currentInterface.equals("Graphic")
                ? new GraphicAdminView(localizationManager)
                : new CliAdminView(localizationManager);
    }
}
