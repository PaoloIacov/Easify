package model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private ConnectionFactory() {}

    private static Properties properties;

    // Blocco statico per caricare le proprietà al momento del caricamento della classe
    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties = new Properties();
            if (input == null) {
                throw new IOException("File database.properties non trovato.");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Errore durante il caricamento delle proprietà del database.");
        }
    }

    // Metodo per ottenere la connessione generica
    public static Connection getConnection() throws SQLException {
        String connectionUrl = properties.getProperty("CONNECTION_URL");
        String user = properties.getProperty("LOGIN_USER");
        String pass = properties.getProperty("LOGIN_PASS");

        return DriverManager.getConnection(connectionUrl, user, pass);
    }

}
