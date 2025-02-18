package view.LoginView;

import controller.LoginController;
import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import model.localization.LocalizationManager;
import java.sql.SQLException;
import java.util.Scanner;

public class CliLoginView implements LoginView {

    private final Scanner scanner;
    private final LocalizationManager localizationManager;
    private final LoginController loginController;

    public CliLoginView(LocalizationManager localizationManager, LoginController loginController) {
        this.scanner = new Scanner(System.in);
        this.localizationManager = localizationManager;
        this.loginController = loginController;
    }

    @Override
    public void display() {
        while (true) {
            System.out.println(localizationManager.getText("login.title"));

            try {
                CredentialsBean credentials = getCredentialsInput();
                credentials = loginController.authenticate(credentials);
                showSuccess();
                loginController.navigateBasedOnRole(credentials);
                break; // Uscire dal loop dopo il login riuscito
            } catch (SQLException e) {
                showError(localizationManager.getText("error.database"));
            } catch (LoginException e) {
                showError(e.getMessage());
            } catch (Exception e) {
                showError(localizationManager.getText("login.error"));
            }
        }
    }

    @Override
    public CredentialsBean getCredentialsInput() {
        System.out.print(localizationManager.getText("login.username") + ": ");
        String username = scanner.nextLine();
        System.out.print(localizationManager.getText("login.password") + ": ");
        String password = scanner.nextLine();
        return new CredentialsBean(username, password);
    }

    @Override
    public void showSuccess() {
        System.out.println(localizationManager.getText("login.success"));
    }

    @Override
    public void showError(String errorMessage) {
        System.out.println(localizationManager.getText("error.title") + ": " + errorMessage);
    }

    @Override
    public void close() {
        System.out.println(localizationManager.getText("login.close"));
    }

    @Override
    public void refresh() {
        System.out.println(localizationManager.getText("login.refresh"));
    }

    @Override
    public void back() {
        System.out.println(localizationManager.getText("login.back"));
    }

    @Override
    public String getInput(String promptKey) {
        System.out.print(localizationManager.getText(promptKey) + ": ");
        return scanner.nextLine();
    }

    @Override
    public boolean isGraphic() {
        return false;
    }
}
