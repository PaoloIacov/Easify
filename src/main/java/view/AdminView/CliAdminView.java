package view.AdminView;

import controller.ActionHandler;
import model.bean.UserBean;
import model.localization.LocalizationManager;

import java.util.List;
import java.util.Scanner;

public class CliAdminView implements AdminView {

    private final LocalizationManager localizationManager;
    private final Scanner scanner = new Scanner(System.in);
    private String selectedUsername;

    public CliAdminView(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;
    }

    @Override
    public void display() {
        System.out.println("\n=== " + localizationManager.getText("admin.menu.title") + " ===");
        System.out.println(localizationManager.getText("admin.menu.options"));
    }

    @Override
    public void displayAllUsers(List<UserBean> users) {
        System.out.println(localizationManager.getText("admin.users.title"));
        if (users.isEmpty()) {
            System.out.println(localizationManager.getText("admin.users.empty"));
        } else {
            int index = 1;
            for (UserBean user : users) {
                System.out.printf("[%d]%n", index++);
                System.out.println(user);
            }
        }
    }


    @Override
    public UserBean addUser() {
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

        return new UserBean(username, password, name, surname, role);
    }

    @Override
    public String removeUser(List<String> usernames) {
        if (usernames.isEmpty()) {
            System.out.println(localizationManager.getText("admin.remove.user.empty"));
            return null;
        }

        System.out.println("\n" + localizationManager.getText("admin.remove.user.list"));
        for (int i = 0; i < usernames.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + usernames.get(i));
        }

        int selectedIndex = -1;
        while (selectedIndex < 1 || selectedIndex > usernames.size()) {
            System.out.print(localizationManager.getText("admin.remove.user.select") + ": ");
            try {
                selectedIndex = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(localizationManager.getText("admin.input.invalid"));
            }
        }

        String selectedUser = usernames.get(selectedIndex - 1);
        System.out.print(localizationManager.getText("admin.confirm") + " (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            return selectedUser;
        } else {
            System.out.println(localizationManager.getText("admin.remove.cancelled"));
            return null;
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
    public String getSelectedUsername() {
        return selectedUsername;
    }
    @Override
    public String getInput(String prompt) {
        System.out.print(localizationManager.getText(prompt));
        return scanner.nextLine();
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
        System.out.println(localizationManager.getText("admin.back"));
    }

    @Override
    public void refresh() {
        System.out.println(localizationManager.getText("admin.refresh"));
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("admin.close"));
    }

    @Override
    public boolean isGraphic() {
        return false;
    }

    @Override
    public void setActionHandler(ActionHandler handler) {
        throw new UnsupportedOperationException("CLI does not support action handlers.");
    }
}
