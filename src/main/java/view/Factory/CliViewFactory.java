package view.Factory;

import controller.AdminController;
import controller.ConversationController;
import controller.LoginController;
import controller.ProjectController;
import model.bean.CredentialsBean;
import model.localization.LocalizationManager;
import view.AdminView.AdminView;
import view.AdminView.CliAdminView;
import view.ConversationView.CliConversationView;
import view.ConversationView.ConversationView;
import view.ConversationView.DecoratedConversationView.CliPmConversationViewDecorator;
import view.LoginView.CliLoginView;
import view.LoginView.LoginView;
import view.ProjectView.CliProjectView;
import view.ProjectView.DecoratedProjectView.CliAdminProjectViewDecorator;
import view.ProjectView.ProjectView;

public class CliViewFactory implements ViewFactory {
    private final LocalizationManager localizationManager;

    public CliViewFactory(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
    }

    @Override
    public LoginView createLoginView(LoginController loginController) {
        return new CliLoginView(localizationManager, loginController);
    }

    @Override
    public AdminView createAdminView(AdminController adminController) {
        return new CliAdminView(localizationManager, adminController);
    }

    @Override
    public ProjectView createProjectView(ProjectController projectController, CredentialsBean loggedInUser) {
        return loggedInUser.getRole() == 3
                ? new CliAdminProjectViewDecorator(new CliProjectView(localizationManager, projectController, loggedInUser), localizationManager)
                : new CliProjectView(localizationManager, projectController, loggedInUser);
    }

    @Override
    public ConversationView createConversationView(ConversationController conversationController, String projectName) {
        return projectName != null
                ? new CliPmConversationViewDecorator(new CliConversationView(localizationManager, conversationController), localizationManager)
                : new CliConversationView(localizationManager, conversationController);
    }
}

