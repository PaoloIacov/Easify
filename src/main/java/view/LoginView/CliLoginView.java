package view.LoginView;

import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import model.localization.LocalizationManager;
import view.GeneralUtils;

import java.util.Scanner;

public class CliLoginView implements LoginView {

    private final Scanner scanner;
    private final LocalizationManager localizationManager;

    public CliLoginView(LocalizationManager localizationManager) {
        this.scanner = new Scanner(System.in);
        this.localizationManager = localizationManager;
    }

    @Override
    public CredentialsBean getCredentialsInput() throws LoginException {
        try {
            System.out.print(localizationManager.getText("login.username") + ": ");
            String username = scanner.nextLine();
            System.out.print(localizationManager.getText("login.password") + ": ");
            String password = scanner.nextLine();
            return new CredentialsBean(username, password);
        } catch (NumberFormatException e) {
            throw new LoginException(localizationManager.getText("error.invalid.role")) {
            };
        }
    }

    public void showSuccess() {
        System.out.println(localizationManager.getText("login.success"));
    }

    @Override
    public void showError(String errorMessage) {
        System.out.println(localizationManager.getText("login.error"));
    }

    @Override
    public void display() {
        System.out.println(localizationManager.getText("login.title"));
    }

    @Override
    public void close() {
        try {
            scanner.close();
        } catch (Exception e) {
            System.out.println(localizationManager.getText("login.close"));
        }
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
        return GeneralUtils.getnput(localizationManager, promptKey);

    }

    @Override
    public boolean isGraphic() {
        return false;
    }
}
