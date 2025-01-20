package model.bean;

import java.io.Serializable;

public class CredentialsBean implements Serializable {

    private String username;

    public CredentialsBean() {}

    public CredentialsBean(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

