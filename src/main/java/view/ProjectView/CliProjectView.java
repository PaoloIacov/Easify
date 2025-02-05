package view.ProjectView;

import controller.ActionHandler;
import model.bean.ProjectBean;
import model.localization.LocalizationManager;
import view.GeneralUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliProjectView implements ProjectView {
    private final LocalizationManager localizationManager;
    private final Scanner scanner;
    private List<ProjectBean> projectList;
    private static final String PROJECT_LIST_EMPTY = "project.list.empty";
    private static final String INVALID_SELECTION = "error.invalid.selection";

    public CliProjectView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
        this.scanner = new Scanner(System.in);
        this.projectList = new ArrayList<>();
    }

    @Override
    public void display() {
        System.out.println(localizationManager.getText("project.menu.title"));
        System.out.println(localizationManager.getText("project.menu.options"));
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("view.close"));
    }

    @Override
    public void refresh() {
        System.out.println(localizationManager.getText("view.refresh"));
    }

    @Override
    public void back() {
        System.out.println(localizationManager.getText("view.back"));
    }

    @Override
    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey));
        return scanner.nextLine().trim();
    }

    @Override
    public boolean isGraphic() {
        return false;
    }

    @Override
    public void displayProjects(List<ProjectBean> projects) {
        projectList = new ArrayList<>(projects);
        System.out.println("\n" + localizationManager.getText("project.list.title"));

        if (projects.isEmpty()) {
            System.out.println(localizationManager.getText(PROJECT_LIST_EMPTY));
        } else {
            int index = 1;
            for (ProjectBean project : projects) {
                System.out.println("--------------------------------------------------");
                System.out.println("[" + index + "] " + project.getName());
                System.out.println("  " + localizationManager.getText("project.description") + ": " + project.getDescription());
                System.out.println("--------------------------------------------------");
                index++;
            }
        }
    }

    @Override
    public String showRemoveUserFromProjectDialog(List<String> usernames) {
        System.out.println(localizationManager.getText("admin.remove.user.prompt"));
        GeneralUtils.printList(usernames, null);
        System.out.print(localizationManager.getText("admin.remove.user.select") + ": ");

        return GeneralUtils.selectUsername(scanner, usernames, localizationManager.getText("admin.input.invalid"));
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
        System.err.println(localizationManager.getText(message));
    }

    @Override
    public void addUserToProject(String projectName, String username) {
        System.out.println(localizationManager.getText("project.user.added") + ": " + username + " -> " + projectName);
    }

    @Override
    public void removeUserFromProject(String projectName, String username) {
        System.out.println(localizationManager.getText("project.user.removed") + ": " + username + " -> " + projectName);
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        throw new UnsupportedOperationException("CLI does not support action handlers.");
    }

    @Override
    public String getSelectedProjectName() {
        if (projectList.isEmpty()) {
            System.out.println(localizationManager.getText(PROJECT_LIST_EMPTY));
            return null;
        }

        System.out.print(localizationManager.getText("project.select.prompt") + ": ");
        try {
            int selectedIndex = Integer.parseInt(scanner.nextLine().trim());
            if (!isValidIndex(selectedIndex, projectList.size(), INVALID_SELECTION, localizationManager)) {
                return null;
            }

            return projectList.get(selectedIndex - 1).getName();

        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText(INVALID_SELECTION));
            return null;
        }
    }


    @Override
    public String getSelectedProjectDescription() {
        if (projectList.isEmpty()) {
            System.out.println(localizationManager.getText(PROJECT_LIST_EMPTY));
            return null;
        }

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
            if (!isValidIndex(choice, projectList.size(), INVALID_SELECTION, localizationManager)) {
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(localizationManager.getText(INVALID_SELECTION));
            return null;
        }

        String selectedProjectDescription = projectList.get(choice - 1).getDescription();
        System.out.println(localizationManager.getText("project.selected") + ": " + selectedProjectDescription);
        return selectedProjectDescription;
    }

    public static boolean isValidIndex(int index, int listSize, String errorMessage, LocalizationManager localizationManager) {
        if (index < 1 || index > listSize) {
            System.out.println(localizationManager.getText(errorMessage));
            return false;
        }
        return true;
    }
}
