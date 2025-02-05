package model.dao;

import model.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    private UserUtils() {
    }

    public static List<User> extractUsersFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int role = resultSet.getInt("role");

            User user = new User(username, password, name, surname, role);
            users.add(user);
        }
        return users;
    }
}
