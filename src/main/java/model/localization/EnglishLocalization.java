package model.localization;

import java.util.HashMap;
import java.util.Map;

public class EnglishLocalization implements Localization {

    private final Map<String, String> translations;

    public EnglishLocalization() {
        translations = new HashMap<>();

        // Login
        translations.put("login.title", "\n=== Login Page ===");
        translations.put("login.username", "Username");
        translations.put("login.password", "Password");
        translations.put("login.submit", "Submit");
        translations.put("login.forgotPassword", "Forgot Password?");
        translations.put("login.success", "Login successful");
        translations.put("login.error", "Invalid username or password.");
        translations.put("login.failed", "Login failed. Please try again.");
        translations.put("error.empty.credentials", "Please fill in all fields.");
        translations.put("error.invalid.credentials", "Invalid credentials. Please try again.");
        translations.put("welcome.user", "Welcome to Easify!");

        // Admin
        translations.put("admin.menu.title", "Admin Menu");
        translations.put("admin.menu.options", "1. View all users\n2. Add user\n3. Remove user\n4. Go to projects\n5. Back\n");
        translations.put("admin.menu.prompt", "Enter your choice (1-7): ");
        translations.put("admin.users.title", "All Users");
        translations.put("admin.users.empty", "No users found.");
        translations.put("admin.add.user", "Add User");
        translations.put("user.username", "Username");
        translations.put("user.password", "Password");
        translations.put("user.name", "Name");
        translations.put("user.surname", "Surname");
        translations.put("user.role", "Role");
        translations.put("admin.remove.user", "Remove User");
        translations.put("admin.remove.user.prompt", "Enter the username of the user to remove: ");
        translations.put("admin.confirm", "Confirm");
        translations.put("project.name", "Project Name");
        translations.put("project.description", "Project Description");
        translations.put("admin.navigate.project", "Navigating to Project");
        translations.put("admin.navigate.project.prompt", "Enter the project you want to go to: ");
        translations.put("admin.menu.navigate.project", "Navigate to projects");
        translations.put("admin.add.error", "Error occurred: failed to add user.");
        translations.put("admin.remove.user.empty", "No users available to remove.");
        translations.put("admin.remove.user.list", "Select a user to remove:");
        translations.put("admin.remove.user.select", "Enter the number of the user to remove");
        translations.put("admin.input.invalid", "Invalid input. Please enter a number.");
        translations.put("admin.remove.cancelled", "User removal cancelled.");
        translations.put("field.username.not.null", "Username cannot be empty.");
        translations.put("field.password.not.null", "Password cannot be empty.");
        translations.put("field.role.not.null", "Role cannot be empty.");
        translations.put("field.name.not.null", "Name cannot be empty.");
        translations.put("field.surname.not.null", "Surname cannot be empty.");


        // General
        translations.put("generic.back", "Back");
        translations.put("generic.select", "Select");
        translations.put("generic.error", "An error occurred. Please try again.");
        translations.put("error.title", "Error");
        translations.put("success.title", "Success");
        translations.put("role.employee", "Employee");
        translations.put("role.projectManager", "Project Manager");
        translations.put("role.admin", "Admin");
        translations.put("role.unknownRole", "Unknown role");
        translations.put("interface.choose", "Choose interface:");
        translations.put("interface.graphic", "Graphical User Interface");
        translations.put("interface.cli", "Command Line Interface");
        translations.put("interface.selected", "Selected interface");
        translations.put("interface.prompt", "Enter your choice");
        translations.put("interface.", "Invalid choice. Please try again.");
        translations.put("error.no.previous.view", "No previous view to go back to.");
        translations.put("view.closed", "Closing the page.");
        translations.put("generic.action.success", "Action successful\n");
        translations.put("generic.action.canelled", "Action cancelled\n");
        translations.put("error.invalid.selection", "Invalid selection, try again");
        translations.put("user.not.found", "User not found");
        translations.put("error.invalid.option", "Invalid option selected, please try again.");

        // Project
        translations.put("project.view.title", "Project View");
        translations.put("project.add.prompt.description", "Enter the project description:");
        translations.put("project.add.success", "Project added successfully.");
        translations.put("project.delete.prompt.name", "Enter the project name to delete:");
        translations.put("project.delete.success", "Project deleted successfully.");
        translations.put("error.load.projects", "Error loading projects");
        translations.put("success.add.user.project", "User added to project successfully");
        translations.put("error.add.user.project", "Error adding user to project");
        translations.put("success.remove.user.project", "User removed from project successfully");
        translations.put("error.remove.user.project", "Error removing user from project");
        translations.put("error.navigate.project.details", "Error navigating to project details");
        translations.put("success.add.project", "Project added successfully");
        translations.put("error.add.project", "Error adding project");
        translations.put("success.delete.project", "Project deleted successfully");
        translations.put("error.delete.project", "Error deleting project");
        translations.put("project.list.title", "Project List");
        translations.put("project.list.empty", "No projects found.");
        translations.put("project.menu.title", "Project Menu");
        translations.put("project.menu.options", "1. View all projects\n2. Add employee to project\n3. Delete employee from project\n4. Back");
        translations.put("project.menu.admin.options","5. Create new project\n6. Delete project");
        translations.put("project.menu.prompt", "Enter your choice:");
        translations.put("project.option.invalid", "Invalid option. Please try again.");
        translations.put("project.name.prompt", "Enter the project name:");
        translations.put("project.add.user.prompt.username", "Enter the username to add:");
        translations.put("project.add.user.success", "User added to project successfully.");
        translations.put("project.add.user.error", "Failed to add user to project");
        translations.put("project.remove.user.success", "User removed from project successfully.");
        translations.put("project.remove.user.error", "Failed to remove user from project");
        translations.put("project.details.prompt", "Enter the project name to view details:");
        translations.put("project.details.error", "Failed to navigate to project details");
        translations.put("project.add.error", "Failed to add project");
        translations.put("project.delete.error", "Failed to delete project");
        translations.put("project.button.add.user", "Add employee");
        translations.put("project.button.remove.user", "Remove employee");
        translations.put("project.button.view.conversations", "Go to conversations");
        translations.put("project.remove.user.empty", "No users found in this project.");
        translations.put("project.remove.user.title", "Remove User from Project");
        translations.put("project.not.null", "Project name or description cannot be empty.");
        translations.put("project.details.view", "\nProject");
        translations.put("project.add.user.error.already.in.project", "User is already in the project.");


        // Admin Project
        translations.put("admin.options", "Admin Options:");
        translations.put("admin.add.project", "Add Project");
        translations.put("admin.remove.project", "Delete Project");
        translations.put("admin.option.prompt", "Enter your choice:");
        translations.put("admin.option.invalid", "Invalid option. Please try again.");
        translations.put("admin.remove.project.success", "Project deleted successfully.");

        // Conversation
        translations.put("conversation.view.title", "Conversation View");
        translations.put("conversation.details.view", "Conversation Details");
        translations.put("conversation.no.selected", "No conversation selected.");
        translations.put("conversation.button.send", "Send Message");
        translations.put("conversation.button.view.messages", "View Messages");
        translations.put("conversation.list.title", "Conversations");
        translations.put("conversation.no.conversations", "No conversations available.");
        translations.put("conversation.message.prompt", "Enter your message: ");
        translations.put("conversation.message.sent", "Message sent successfully!");
        translations.put("conversation.error.sending", "Failed to send message.");
        translations.put("conversation.error.loading", "Failed to load conversations.");
        translations.put("conversation.no.permission", "You don't have permission to access this conversation.");
        translations.put("conversation.message.input.reset", "Message input cleared.");
        translations.put("conversation.menu.title", "Conversation Menu");
        translations.put("conversation.menu.options", "1) View Messages\n2) Send Message\n3) Back");
        translations.put("conversation.list.empty", "No conversations available.");
        translations.put("conversation.messages", "Messages in this conversation:");
        translations.put("conversation.messages.empty", "No messages in this conversation.");
        translations.put("conversation.message.reset", "Message input cleared");
        translations.put("conversation.selected", "Selected conversation");
        translations.put("view.close", "Closing view...");
        translations.put("view.refresh", "Refreshing view...");
        translations.put("view.back", "Returning to previous view...");
        translations.put("conversation.menu.prompt", "Enter your choice");
        translations.put("conversation.option.invalid", "Invalid option selected");
        translations.put("conversation.add.prompt.description", "Enter conversation description");
        translations.put("conversation.add.error.empty.description", "Conversation description cannot be empty");
        translations.put("conversation.add.success", "Conversation added successfully");
        translations.put("conversation.add.error", "Failed to add conversation");
        translations.put("conversation.delete.prompt", "Select the conversation to delete");
        translations.put("conversation.delete.select", "Choose a conversation number");
        translations.put("conversation.delete.invalid", "Invalid selection. Please enter a valid number.");
        translations.put("conversation.delete.success", "Conversation deleted successfully");
        translations.put("conversation.delete.error", "Error deleting conversation");
        translations.put("conversation.delete.permission.denied", "You do not have permission to delete conversations");
        translations.put("admin.option.add.project", "Add Project");
        translations.put("admin.option.delete.project", "Delete Project");
        translations.put("conversation.button.create", "Create Conversation");
        translations.put("conversation.button.delete", "Delete Conversation");
        translations.put("conversation.not.null", "Conversation description cannot be empty.");
        translations.put("conversation.add.user.prompt", "Select a user to add to the conversation:");
        translations.put("conversation.add.user.success", "The user has been successfully added to the conversation.");
        translations.put("conversation.add.user.error", "Error adding the user to the conversation.");
        translations.put("conversation.remove.user.prompt", "Select a user to remove from the conversation:");
        translations.put("conversation.remove.user.success", "The user has been successfully removed from the conversation.");
        translations.put("conversation.remove.user.error", "Error removing the user from the conversation.");
        translations.put("conversation.button.add.user", "Add User");
        translations.put("conversation.button.remove.user", "Remove User");
        translations.put("conversation.select.user", "Add a user to the conversation:");
        translations.put("conversation.unselect.user", "Remove a user from the conversation:");
        translations.put("conversation.user.not.found", "No user found for the conversation.");
        translations.put("conversation.error.add.user.generic", "Generic error while adding a user.");
        translations.put("conversation.error.remove.user.generic", "Generic error while removing a user.");
        translations.put("conversation.error.permission.denied", "You don't have permission to modify this conversation.");
    }

    @Override
    public String getText(String key) {
        return translations.getOrDefault(key, "Key not found: " + key);
    }
}
