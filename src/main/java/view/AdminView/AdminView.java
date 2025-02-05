package view.AdminView;

import controller.ActionHandler;
import model.bean.UserBean;
import view.View;

import java.util.List;

public interface AdminView extends View {
    void displayAllUsers(List<UserBean> users);

    void displayUserDetails(UserBean user);

    UserBean addUser();

    String removeUser(List<String> usernames);

    String getSelectedUsername();

    void showSuccess(String messageKey);

    void showError(String messageKey);

    void setActionHandler(ActionHandler handler);


}
