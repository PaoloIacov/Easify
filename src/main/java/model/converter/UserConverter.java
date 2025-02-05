package model.converter;

import model.bean.UserBean;
import model.domain.User;

public class UserConverter {

    private UserConverter() {
    }

    public static UserBean toBean(User user) {
        if (user == null) {
            return null;
        }
        return new UserBean(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getRole()
        );
    }
}
