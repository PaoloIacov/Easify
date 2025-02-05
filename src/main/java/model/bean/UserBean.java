package model.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    private String username;
    private String password;
    private String name;
    private String surname;
    private int role;

    public UserBean() {
    }

    public UserBean(String username, String password, String name, String surname, int role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return """
        -------------------------
        Username: %s
        Name:     %s %s
        Role:     %s
        -------------------------
        """.formatted(
                this.username,
                this.name,
                this.surname,
                getRoleName(this.role)
        );
    }

    // Helper method to convert role ID to a readable name
    private String getRoleName(int role) {
        return switch (role) {
            case 1 -> "Employee";
            case 2 -> "Project Manager";
            case 3 -> "Admin";
            default -> "Unknown";
        };
    }
}

