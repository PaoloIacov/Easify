package view.Factory;

import controller.AdminController;
import controller.ConversationController;
import controller.LoginController;
import controller.ProjectController;
import model.bean.CredentialsBean;
import model.localization.LocalizationManager;
import view.AdminView.AdminView;
import view.AdminView.GraphicAdminView;
import view.ConversationView.ConversationView;
import view.ConversationView.DecoratedConversationView.GraphicPmConversationViewDecorator;
import view.ConversationView.GraphicConversationView;
import view.LoginView.GraphicLoginView;
import view.LoginView.LoginView;
import view.ProjectView.DecoratedProjectView.GraphicAdminProjectViewDecorator;
import view.ProjectView.GraphicProjectView;
import view.ProjectView.ProjectView;

import java.sql.SQLException;

public class GraphicViewFactory implements ViewFactory {
    private final LocalizationManager localizationManager;

    public GraphicViewFactory(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
    }

    @Override
    public LoginView createLoginView(LoginController loginController) {
        return new GraphicLoginView(localizationManager, loginController);
    }

    @Override
    public AdminView createAdminView(AdminController adminController) throws SQLException {
        return new GraphicAdminView(localizationManager, adminController);
    }

    @Override
    public ProjectView createProjectView(ProjectController projectController, CredentialsBean loggedInUser) {
        return loggedInUser.getRole() == 3
                ? new GraphicAdminProjectViewDecorator(new GraphicProjectView(localizationManager, projectController, loggedInUser, null), localizationManager, projectController, loggedInUser)
                : new GraphicProjectView(localizationManager, projectController, loggedInUser, null);
    }

    @Override
    public ConversationView createConversationView(ConversationController conversationController, String projectName) {
        return projectName != null
                ? new GraphicPmConversationViewDecorator(new GraphicConversationView(localizationManager, conversationController), localizationManager, conversationController)
                : new GraphicConversationView(localizationManager, conversationController);
    }
}

