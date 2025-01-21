package view.LoginView;

import model.bean.CredentialsBean;
import model.localization.LocalizationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliLoginView implements LoginView {

    private final BufferedReader reader;
    private final LocalizationManager localizationManager;

    public CliLoginView(LocalizationManager localizationManager) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.localizationManager = localizationManager;
    }

    public void showLoginScreen() {
        System.out.println("=== " + localizationManager.getText("login.title") + " ===");
    }

    public CredentialsBean getCredentialsInput() {
        try {
            System.out.print(localizationManager.getText("login.username") + ": ");
            String username = reader.readLine();
            System.out.print(localizationManager.getText("login.password") + ": ");
            String password = reader.readLine();
            return new CredentialsBean(username, password);
        } catch (IOException e) {
            throw new RuntimeException(localizationManager.getText("error.input.read"), e);
        }
    }


    @Override
    public void showSuccess(String message) {
        System.out.println(localizationManager.getText("login.success") + ": " + message);
    }

    @Override
    public void showError(String errorMessage) {
        System.out.println(localizationManager.getText("login.error") + errorMessage);
    }

    @Override
    public void display() {
        showLoginScreen();
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
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
}
