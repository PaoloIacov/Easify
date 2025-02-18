package view.AdminView;

import controller.AdminController;
import controller.exceptions.NullFieldException;
import model.bean.UserBean;
import model.localization.LocalizationManager;
import view.GeneralUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CliAdminView implements AdminView {

    private final LocalizationManager localizationManager;
    private final AdminController adminController;
    private final Scanner scanner = new Scanner(System.in);

    public CliAdminView(LocalizationManager localizationManager, AdminController adminController) {
        this.localizationManager = localizationManager;
        this.adminController = adminController;
    }

    @Override
    public void display() {
        while (true) {
            System.out.println("\n=== " + localizationManager.getText("admin.menu.title") + " ===");
            System.out.println(localizationManager.getText("admin.menu.options"));
            System.out.print(localizationManager.getText("interface.prompt"));
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1" -> displayAllUsers();
                case "2" -> addUser();
                case "3" -> removeUser();
                case "4" -> adminController.navigateToProjectView();
                case "5" -> {
                    back();
                    return;
                }
                default -> showError(localizationManager.getText("error.invalid.option"));
            }
        }
    }

    @Override
    public void displayAllUsers() {
        try {
            List<UserBean> users = adminController.getAllUsers();
            System.out.println("\n" + localizationManager.getText("admin.users.title"));

            if (users.isEmpty()) {
                System.out.println(localizationManager.getText("admin.users.empty"));
            } else {
                for (UserBean user : users) {
                    System.out.println("-------------------------");
                    System.out.println(localizationManager.getText("user.username") + ": " + user.getUsername());
                    System.out.println(localizationManager.getText("user.name") + ": " + user.getName());
                    System.out.println(localizationManager.getText("user.surname") + ": " + user.getSurname());
                    System.out.println(localizationManager.getText("user.role") + ": " + getRoleName(user.getRole()));
                }
                System.out.println("-------------------------");
            }
        } catch (Exception e) {
            showError(localizationManager.getText("admin.load.users.error"));
        }
    }


    @Override
    public void addUser() {
        try {
            System.out.println("\n" + localizationManager.getText("admin.add.user"));
            System.out.print(localizationManager.getText("user.username") + ": ");
            String username = scanner.nextLine();
            System.out.print(localizationManager.getText("user.password") + ": ");
            String password = scanner.nextLine();
            System.out.print(localizationManager.getText("user.name") + ": ");
            String name = scanner.nextLine();
            System.out.print(localizationManager.getText("user.surname") + ": ");
            String surname = scanner.nextLine();
            System.out.print(localizationManager.getText("user.role") + ": ");
            int role = Integer.parseInt(scanner.nextLine());

            UserBean user = new UserBean(username, password, name, surname, role);
            adminController.addUser(user);

            showSuccess(localizationManager.getText("admin.add.user.success"));
            displayAllUsers();
        } catch (NumberFormatException e) {
            showError(localizationManager.getText("admin.add.user.invalid.role"));
        } catch (NullFieldException | SQLException e) {
            showError(e.getMessage());
        }
    }

    @Override
    public void removeUser() {
        try {
            List<UserBean> users = adminController.getAllUsers();
            List<String> usernames = users.stream().map(UserBean::getUsername).toList();

            if (usernames.isEmpty()) {
                showError(localizationManager.getText("admin.remove.no.users"));
                return;
            }

            System.out.println("\n" + localizationManager.getText("admin.remove.user.list"));
            GeneralUtils.printList(usernames, null);

            int selectedIndex = -1;
            while (selectedIndex < 1 || selectedIndex > usernames.size()) {
                System.out.print(localizationManager.getText("admin.remove.user.select") + ": ");
                selectedIndex = Integer.parseInt(scanner.nextLine());

            }

            String selectedUser = usernames.get(selectedIndex - 1);
            System.out.print(localizationManager.getText("admin.confirm") + " (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                adminController.removeUser(selectedUser);
                showSuccess(localizationManager.getText("generic.action.success"));
                displayAllUsers();
            } else {
                System.out.println(localizationManager.getText("admin.remove.cancelled"));
            }
        } catch (SQLException e) {
            showError(localizationManager.getText("admin.remove.error"));
        }
    }

    @Override
    public void displayUserDetails(UserBean user) {
        System.out.println("\n=== " + localizationManager.getText("admin.user.details.title") + " ===");
        System.out.println(localizationManager.getText("admin.user.details.username") + ": " + user.getUsername());
        System.out.println(localizationManager.getText("admin.user.details.name") + ": " + user.getName());
        System.out.println(localizationManager.getText("admin.user.details.surname") + ": " + user.getSurname());
        System.out.println(localizationManager.getText("admin.user.details.role") + ": " + getRoleName(user.getRole()));
        System.out.println("======================");
    }

    private String getRoleName(int role) {
        return switch (role) {
            case 1 -> localizationManager.getText("role.employee");
            case 2 -> localizationManager.getText("role.projectManager");
            case 3 -> localizationManager.getText("role.admin");
            default -> localizationManager.getText("role.unknownRole");
        };
    }

    @Override
    public void showSuccess(String message) {
        System.out.println("\n" + localizationManager.getText("generic.action.success"));
    }

    @Override
    public void showError(String message) {
        System.err.println("\n" + localizationManager.getText("generic.error"));
    }

    @Override
    public void back() {
      adminController.back();
    }

    @Override
    public void refresh() {
        displayAllUsers();
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("admin.close"));
    }

    @Override
    public boolean isGraphic() {
        return false;
    }

    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey) + ": ");
        return scanner.nextLine().trim();
    }
}
