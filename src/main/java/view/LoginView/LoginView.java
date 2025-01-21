package view.LoginView;

import controller.exceptions.LoginExceptions.InvalidCredentialsException;
import model.bean.CredentialsBean;
import view.View;

public interface LoginView extends View {
    CredentialsBean getCredentialsInput() throws InvalidCredentialsException;
    void showSuccess(String message);
}
