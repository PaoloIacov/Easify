package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.domain.Conversation;
import model.domain.User;

public class ConversationDAO {

    private final Connection connection;
    private static final String DESCRIPTION = "description";
    private static final String PROJECT_NAME = "projectName";

    public ConversationDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Conversation> getConversationsForUser(String username, int role) throws SQLException {
        return switch (role) {
            case 1 -> //Employee
                    getConversationsForEmployee(username);
            case 2, 3 -> //Project Manager or Admin
                    getConversationsForProjectManager(username);
            default -> {
                System.out.println("Role not found");
                yield new ArrayList<>();
            }
        };
    }

    public void addConversation(String description, String projectName) {
        String query = "INSERT INTO Conversation (description, projectName) VALUES (?, ?)";
        try {
            DaoUtils.executeUpdate(connection, query, description, projectName);
        } catch (SQLException e) {
            System.err.println("Error adding conversation: " + e.getMessage());
        }
    }


    public void deleteConversation(Long conversationId) {
        String deleteParticipationQuery = "DELETE FROM ConversationParticipation WHERE conversationID = ?";
        String deleteConversationQuery = "DELETE FROM Conversation WHERE ID = ?";

        try (PreparedStatement pstmtParticipation = connection.prepareStatement(deleteParticipationQuery);
             PreparedStatement pstmtConversation = connection.prepareStatement(deleteConversationQuery)) {

            pstmtParticipation.setLong(1, conversationId);
            pstmtParticipation.executeUpdate();

            pstmtConversation.setLong(1, conversationId);
            pstmtConversation.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting conversation with ID: " + conversationId + e.getMessage());
        }
    }

    public List<Conversation> getConversationsForProject(String projectName) throws SQLException {
        String query = "SELECT id, description, projectName FROM Conversation WHERE projectName = ?";
        return getConversations(query, projectName);
    }

    public List<Conversation> getConversationsForEmployee(String username) throws SQLException {
        String query = """
        SELECT c.id, c.description, c.projectName
        FROM Conversation c
        JOIN ConversationParticipation cp ON c.id = cp.conversationID
        WHERE cp.participant = ?;
    """;

        return getConversations(query, username);
    }


    public List<Conversation> getConversationsForProjectManager(String username) throws SQLException {
        List<Conversation> conversations = new ArrayList<>();

        String query = "SELECT c.ID, c.description, c.projectName " +
                "FROM Conversation c " +
                "JOIN ProjectAssignments pa ON c.projectName = pa.projectName " +
                "WHERE pa.username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Conversation conversation = new Conversation(
                            rs.getLong("ID"),
                            rs.getString(DESCRIPTION),
                            rs.getString(PROJECT_NAME)
                    );
                    conversations.add(conversation);
                }
            }
        }

        return conversations;
    }


    public void addUserToConversation(Long conversationID, String participant) {
        String query = "INSERT INTO ConversationParticipation (conversationID, participant) VALUES (?, ?)";
        executeConversationUpdate(query, conversationID, participant, "Error adding employee to conversation with ID:");
    }


    public void removeUserFromConversation(Long conversationID, String participant) {
        String query = "DELETE FROM ConversationParticipation WHERE conversationID = ? AND participant = ?";
        executeConversationUpdate(query, conversationID, participant, "Error removing employee from conversation with ID:");
    }


    public List<User> getUsersInConversation(Long conversationID) {
        String query = "SELECT u.username, u.password, u.name, u.surname, u.role " +
                "FROM User u " +
                "JOIN ConversationParticipation cp ON u.username = cp.participant " +
                "WHERE cp.conversationID = ? AND u.role = 1";
        return executeUserQuery(query, conversationID);
    }

    public List<User> getUsersNotInConversation(Long conversationId) {
        String query = "SELECT u.username, u.password, u.name, u.surname, u.role " +
                "FROM User u " +
                "WHERE u.username NOT IN ( " +
                "    SELECT cp.participant " +
                "    FROM ConversationParticipation cp " +
                "    WHERE cp.conversationId = ? " +
                ")";

        return executeUserQuery(query, conversationId);
    }

    private List<User> executeUserQuery(String query, long conversationId) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, conversationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    int role = rs.getInt("role");

                    User user = new User(username, password, name, surname, role);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving users from conversation with ID: " + conversationId);
        }
        return users;
    }

    private List<Conversation> getConversations(String query, String parameterValue) throws SQLException {
        List<Conversation> conversations = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, parameterValue);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    conversations.add(new Conversation(
                            rs.getLong("id"),
                            rs.getString(DESCRIPTION),
                            rs.getString(PROJECT_NAME)
                    ));
                }
            }
        }
        return conversations;
    }

    private void executeConversationUpdate(String query, long conversationID, String username, String errorMessage) {
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, conversationID);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(errorMessage + " " + conversationID + ": " + e.getMessage());
        }
    }


}



