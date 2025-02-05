import model.bean.CredentialsBean;
import model.dao.ConnectionFactory;
import model.dao.LoginDAO.DbmsLoginDAO;
import model.domain.Credentials;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginTest {

    private Connection connection;
    private DbmsLoginDAO dbmsLoginDAO;

    @BeforeAll
    public void setupDatabase() throws SQLException {
        connection = ConnectionFactory.getConnection();
        dbmsLoginDAO = new DbmsLoginDAO(connection);

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO User (username, password, role) VALUES (?, ?, ?)")) {
            stmt.setString(1, "testUser");
            stmt.setString(2, "testPassword");
            stmt.setInt(3, 1); // Role: 1 = Employee
            stmt.executeUpdate();
        }
    }

    @AfterAll
    public void cleanupDatabase() throws SQLException {
        // Rimuove i dati di test
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM User WHERE username = ?")) {
            stmt.setString(1, "testUser");
            stmt.executeUpdate();
        }

        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testValidLogin() throws SQLException {
        // Simula input valido
        String simulatedInput = "testUser\ntestPassword\n";
        simulateUserInput(simulatedInput);

        // Recupera le credenziali simulate
        CredentialsBean credentialsBean = getCredentialsInput();

        // Converte in modello e valida
        Credentials credentials = new Credentials();
        credentials.setUsername(credentialsBean.getUsername());
        credentials.setPassword(credentialsBean.getPassword());
        boolean isValid = dbmsLoginDAO.validateCredentials(credentials);

        // Asserzione
        assertTrue(isValid, "Login with valid credentials should pass.");
    }

    @Test
    void testInvalidLogin() throws SQLException {
        // Simula input non valido
        String simulatedInput = "wrongUser\nwrongPassword\n";
        simulateUserInput(simulatedInput);

        // Recupera le credenziali simulate
        CredentialsBean credentialsBean = getCredentialsInput();

        // Converte in modello e valida
        Credentials credentials = new Credentials();
        credentials.setUsername(credentialsBean.getUsername());
        credentials.setPassword(credentialsBean.getPassword());
        boolean isValid = dbmsLoginDAO.validateCredentials(credentials);

        // Asserzione
        assertFalse(isValid, "Login with invalid credentials should fail.");
    }

    // Metodo per simulare l'input dell'utente
    private void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    // Metodo per recuperare le credenziali simulate
    private CredentialsBean getCredentialsInput() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.print("Insert username: ");
        String username = scanner.nextLine();

        System.out.print("Insert password: ");
        String password = scanner.nextLine();

        return new CredentialsBean(username, password);
    }
}
