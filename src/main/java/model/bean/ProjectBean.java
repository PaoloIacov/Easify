package model.bean;

import java.io.Serializable;

public class ProjectBean implements Serializable {

    private String name;
    private String description;

    public ProjectBean() {}

    public ProjectBean(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProjectBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
