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
                user.getPassword(), // ⚠️ Nota: Non è consigliabile esporre le password, gestisci criptazione!
                user.getName(),
                user.getSurname(),
                user.getRole()
        );
    }

    public static User toEntity(UserBean userBean) {
        if (userBean == null) {
            return null;
        }
        return new User(
                userBean.getUsername(),
                userBean.getPassword(), // ⚠️ Nota: Evita di salvare password in chiaro
                userBean.getName(),
                userBean.getSurname(),
                userBean.getRole()
        );
    }
}
