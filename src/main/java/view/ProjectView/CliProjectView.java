package view.ProjectView;

import controller.ProjectController;
import model.bean.CredentialsBean;
import model.bean.ProjectBean;
import model.bean.UserBean;
import model.localization.LocalizationManager;

import java.util.List;
import java.util.Scanner;

public class CliProjectView implements ProjectView {
    private final LocalizationManager localizationManager;
    private final ProjectController projectController;
    private final CredentialsBean loggedInUser;
    private final Scanner scanner;
    private String selectedProjectName;
    private static final String INVALID_OPTION = "error.invalid.option";

    public CliProjectView(LocalizationManager localizationManager, ProjectController projectController, CredentialsBean loggedInUser) {
        this.localizationManager = localizationManager;
        this.projectController = projectController;
        this.loggedInUser = loggedInUser;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void display() {
        boolean running = true;

        while (running) {  // 🔹 Mantiene la CLI attiva finché l'utente non sceglie "Indietro"
            System.out.println("\n=== " + localizationManager.getText("project.menu.title") + " ===");
            System.out.println(localizationManager.getText("project.menu.options"));

            System.out.print(localizationManager.getText("interface.prompt"));
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1" -> displayProjects();
                case "2" -> addUserToProject();
                case "3" -> removeUserFromProject();
                case "4" -> projectController.navigateToConversations(selectedProjectName);
                case "5" -> running = false;  // 🔹 Esce dal loop e torna alla vista precedente
                default -> showError(localizationManager.getText(INVALID_OPTION));
            }
        }
    }


    @Override
    public void displayProjects() {
        try {
            List<ProjectBean> projects = projectController.getProjectsForUser(loggedInUser.getUsername());
            if (projects.isEmpty()) {
                showError(localizationManager.getText("project.list.empty"));
                return;
            }
            System.out.println(localizationManager.getText("project.list.title"));
            int index = 1;
            for (ProjectBean project : projects) {
                System.out.println("[" + index + "] " + project.getName() + " - " + project.getDescription());
                index++;
            }
            System.out.print("\n" + localizationManager.getText("project.select.prompt") + ": ");
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (choice < 0 || choice >= projects.size()) {
                showError(localizationManager.getText(INVALID_OPTION));
                return;
            }

            selectedProjectName = projects.get(choice).getName();
            System.out.println(selectedProjectName);

        } catch (NumberFormatException e) {
            showError(localizationManager.getText(INVALID_OPTION));
        }
    }

    @Override
    public void addUserToProject() {
        if (selectedProjectName == null) {
            showError(localizationManager.getText("project.no.selected"));
            return;
        }

        try {
            // Retrieve users not already in the project
            List<UserBean> availableUsers = projectController.getUsersNotInProject(selectedProjectName);

            if (availableUsers.isEmpty()) {
                showError(localizationManager.getText("project.add.user.error.no.available"));
                return;
            }

            // Display list of users
            System.out.println("\n" + localizationManager.getText("project.add.user.prompt.available"));
            for (int i = 0; i < availableUsers.size(); i++) {
                System.out.println((i + 1) + " - " + availableUsers.get(i).getUsername());
            }

            // Prompt user to select an index
            System.out.print(localizationManager.getText("conversation.add.user.select") + ": ");
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;

            // Validate selection
            if (choice < 0 || choice >= availableUsers.size()) {
                showError(localizationManager.getText(INVALID_OPTION));
                return;
            }

            // Get selected username and add user to project
            String selectedUsername = availableUsers.get(choice).getUsername();
            projectController.addUserToProject(selectedProjectName, selectedUsername);
            showSuccess(localizationManager.getText("project.add.user.success"));

        } catch (NumberFormatException e) {
            showError(localizationManager.getText(INVALID_OPTION));
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }


    @Override
    public void removeUserFromProject() {
        if (selectedProjectName == null) {
            showError(localizationManager.getText("project.no.selected"));
            return;
        }

        try {
            List<String> usernames = projectController.getUsersInProject(selectedProjectName)
                    .stream()
                    .map(UserBean::getUsername)
                    .toList();

            if (usernames.isEmpty()) {
                showError(localizationManager.getText("project.remove.user.empty"));
                return;
            }

            String selectedUsername = showRemoveUserFromProjectDialog(usernames);

            if (selectedUsername != null) {
                projectController.removeUserFromProject(selectedProjectName, selectedUsername);
                showSuccess(localizationManager.getText("project.remove.user.success"));
            }

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    @Override
    public String showRemoveUserFromProjectDialog(List<String> usernames) {
        System.out.println(localizationManager.getText("admin.remove.user.prompt"));
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println((i + 1) + " - " + usernames.get(i));
        }
        System.out.print(localizationManager.getText("admin.remove.user.select") + ": ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice < 0 || choice >= usernames.size()) {
                showError(localizationManager.getText(INVALID_OPTION));
                return null;
            }
            return usernames.get(choice);
        } catch (NumberFormatException e) {
            showError(localizationManager.getText(INVALID_OPTION));
            return null;
        }
    }

    public void navigateToConversations() {

        projectController.navigateToConversations(selectedProjectName);
    }

    @Override
    public void navigateToProjectDetails(String projectName, String projectDescription) {
        System.out.println(localizationManager.getText("project.details.view") + ": " + projectName);
    }

    @Override
    public void showSuccess(String message) {
        System.out.println(localizationManager.getText("success.title") + ": " + message);
    }

    @Override
    public void showError(String message) {
        System.err.println(localizationManager.getText("error.title") + ": " + message);
    }

    @Override
    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey) + ": ");
        return scanner.nextLine().trim();
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("view.close"));
    }

    @Override
    public void refresh() {
        displayProjects();
    }

    @Override
    public void back() {
        close();
    }

    @Override
    public boolean isGraphic() {
        return false;
    }
}
