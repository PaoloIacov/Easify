package model.dao;

import model.domain.Project;
import model.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    private final Connection connection;

    public ProjectDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Project> getProjectsForUser(String username) throws SQLException {
        List<Project> projects = new ArrayList<>();
        UserDAO userDAO = new UserDAO(connection);

        int role = userDAO.getUserRole(username);

        switch (role) {
            case 3: //Admin
                return getAllProjects();

            case 2: //PM
                String query = "SELECT p.name, p.description " +
                        "FROM Project p " +
                        "JOIN ProjectAssignments pa ON p.name = pa.projectName " +
                        "WHERE pa.username = ?";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            String projectName = resultSet.getString("name");
                            String projectDescription = resultSet.getString("description");
                            Project project = new Project(projectName, projectDescription);
                            projects.add(project);
                        }
                    }
                }
                break;

            default:
                System.out.println("Ruolo non supportato per l'utente: " + username);
                break;
        }

        return projects;
    }

    public List<Project> getAllProjects() throws SQLException {
        List<Project> projects = new ArrayList<>();

        String query = "SELECT name, description FROM Project";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String projectName = resultSet.getString("name");
                    String projectDescription = resultSet.getString("description");
                    Project project = new Project(projectName, projectDescription);
                    projects.add(project);
                }
            }
        }

        return projects;
    }

    public void addProject(String projectName, String projectDescription) throws SQLException {
        String query = "INSERT INTO Project (name, description) VALUES (?, ?)";
        DaoUtils.executeUpdate(connection, query, projectName, projectDescription);
    }


    public void addEmployeeToProject(String projectName, String username) throws SQLException {
        String query = "INSERT INTO ProjectAssignments (projectName, username) VALUES (?, ?)";
        DaoUtils.executeUpdate(connection, query, projectName, username);
    }

    public void deleteProject(String projectName) throws SQLException {
        // First, delete all conversations related to the project
        String deleteConversationsQuery = "DELETE FROM Conversation WHERE projectName = ?";

        try (PreparedStatement pstmtConversations = connection.prepareStatement(deleteConversationsQuery)) {
            pstmtConversations.setString(1, projectName);
            pstmtConversations.executeUpdate();
        }

        // Now, delete the project itself
        String deleteProjectQuery = "DELETE FROM Project WHERE name = ?";
        try (PreparedStatement pstmtProject = connection.prepareStatement(deleteProjectQuery)) {
            pstmtProject.setString(1, projectName);
            pstmtProject.executeUpdate();
        }
    }


    public void removeEmployeeFromProject(String projectName, String username) throws SQLException {
        String query = "DELETE FROM ProjectAssignments WHERE projectName = ? AND username = ?";
        DaoUtils.executeUpdate(connection, query, projectName, username);
    }


    public List<User> getUsersFromProject(String projectName) {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.username, u.password, u.name, u.surname, u.role " +
                "FROM User u " +
                "JOIN ProjectAssignments pa ON u.username = pa.username " +
                "WHERE pa.projectName = ? AND u.role = 1"; // Solo Employee (role = 1)

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, projectName);
            try (ResultSet resultSet = statement.executeQuery()) {
                users.addAll(DaoUtils.extractUsersFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero degli utenti del progetto " + projectName);
        }

        return users;
    }

    public List<User> getUsersNotInProject(String projectName) {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.username, u.password, u.name, u.surname, u.role " +
                "FROM User u " +
                "WHERE u.username NOT IN (SELECT pa.username FROM ProjectAssignments pa WHERE pa.projectName = ?) AND u.role = 1"; // Solo Employee (role = 1)

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, projectName);
            try (ResultSet resultSet = statement.executeQuery()) {
                users.addAll(DaoUtils.extractUsersFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero degli utenti non assegnati al progetto " + projectName);
        }

        return users;
    }

}
