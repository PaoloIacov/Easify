package view.LoginView;

import controller.exceptions.LoginExceptions.LoginException;
import model.bean.CredentialsBean;
import view.View;

public interface LoginView extends View {
    CredentialsBean getCredentialsInput() throws LoginException;
    void showSuccess();
}
