package view.Factory;

import controller.AdminController;
import controller.ConversationController;
import controller.LoginController;
import controller.ProjectController;
import model.bean.CredentialsBean;
import view.AdminView.AdminView;
import view.ConversationView.ConversationView;
import view.LoginView.LoginView;
import view.ProjectView.ProjectView;

import java.sql.SQLException;

public interface ViewFactory {
    LoginView createLoginView(LoginController loginController);
    AdminView createAdminView(AdminController adminController) throws SQLException;
    ProjectView createProjectView(ProjectController projectController, CredentialsBean loggedInUser);
    ConversationView createConversationView(ConversationController conversationController, String projectName);
}
