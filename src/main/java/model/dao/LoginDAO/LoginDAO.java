package model.dao.LoginDAO;

import model.domain.Credentials;

import java.sql.SQLException;

public interface LoginDAO {
    boolean validateCredentials(Credentials credentials) throws SQLException;
}
