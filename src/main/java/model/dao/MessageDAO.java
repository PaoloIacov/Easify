package model.dao;

import model.domain.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO{

    private final Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public void addMessage(Message message) {
        String query = "INSERT INTO Message (conversationID, senderUsername, content, datetime) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, message.getConversationID());
            stmt.setString(2, message.getSenderUsername());
            stmt.setString(3, message.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(message.getDatetime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding message: " + e.getMessage());
        }
    }

    public List<Message> getMessagesByConversationID(Long conversationID) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Message WHERE conversationID = ? ORDER BY datetime ";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, conversationID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                        rs.getLong("conversationID"),
                        rs.getString("senderUsername"),
                        rs.getString("content"),
                        rs.getTimestamp("datetime").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        return messages;
    }
}
