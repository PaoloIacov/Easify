package model.dao.LoginDAO;

import model.domain.Credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbmsLoginDAO implements LoginDAO {

    private final Connection connection;

    public DbmsLoginDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean validateCredentials(Credentials credentials) throws SQLException {
        String query = "SELECT username, password, role FROM User WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, credentials.getUsername());
            stmt.setString(2, credentials.getPassword());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    credentials.setRole(rs.getInt("role")); // Imposta il ruolo nel bean
                    return true;
                }
            }
        }
        return false;
    }
}
