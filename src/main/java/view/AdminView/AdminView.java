package view.AdminView;

import model.bean.UserBean;
import view.View;

import java.sql.SQLException;

public interface AdminView extends View {
    void displayAllUsers() throws SQLException;

    void displayUserDetails(UserBean user);

    void addUser();

    void removeUser();

    void showSuccess(String messageKey);

    void showError(String messageKey);

}
