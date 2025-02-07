package model.dao.LoginDAO;

import model.domain.Credentials;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class FileSystemLoginDAO implements LoginDAO {

    private final File credentialsFile;

    public FileSystemLoginDAO(String filePath) {
        this.credentialsFile = new File(filePath);
    }

    @Override
    public boolean validateCredentials(Credentials credentials) throws SQLException {
        if (!credentialsFile.exists() || !credentialsFile.canRead()) {
            throw new SQLException("Login file not found or cannot be read");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUsername = parts[0].trim();
                    String storedPassword = parts[1].trim();

                    if (storedUsername.equals(credentials.getUsername()) &&
                            storedPassword.equals(credentials.getPassword())) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Error reading login file: " + e.getMessage());
        }

        return false;
    }
}
