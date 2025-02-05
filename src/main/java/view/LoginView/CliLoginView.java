package view.LoginView;

import controller.exceptions.LoginExceptions.LoginException;
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

    @Override
    public CredentialsBean getCredentialsInput() throws LoginException {
        try {
            System.out.print(localizationManager.getText("login.username") + ": ");
            String username = reader.readLine();
            System.out.print(localizationManager.getText("login.password") + ": ");
            String password = reader.readLine();
            return new CredentialsBean(username, password);
        } catch (IOException e) {
           throw new LoginException(localizationManager.getText("error.input.read"), e) {
           };
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
            reader.close();
        } catch (IOException e) {
            System.err.println(localizationManager.getText("generic.error"));
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
        System.out.print(localizationManager.getText(promptKey) + ": ");
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(localizationManager.getText("error.input.read"), e) {
            };
        }
    }

    @Override
    public boolean isGraphic() {
        return false;
    }
}
