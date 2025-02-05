package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.domain.User;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public int getUserRole(String username) throws SQLException {
        String query = "SELECT role FROM User WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("role");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT username, password, name, surname, role FROM User";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            return DaoUtils.extractUsersFromResultSet(resultSet);
        }
    }

    public void addUser(String username, String password, String name, String surname, int role) throws SQLException {
        String query = "INSERT INTO User (username, password, name, surname, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, surname);
            statement.setInt(5, role);
            statement.executeUpdate();
        }
    }

    public void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM User WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }

    public boolean isUserInProject(String projectName, String username) throws SQLException {
        String query = "SELECT username, projectName FROM ProjectAssignments WHERE username = ? AND projectName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, projectName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT username, password, name, surname, role FROM User WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getInt("role")
                    );
                }
            }
        }
        return null;
    }

}
