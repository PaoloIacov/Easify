package model.converter;

import model.bean.ProjectBean;
import model.domain.Project;

public class ProjectConverter {

    private ProjectConverter() {
    }

    public static Project toDomain(ProjectBean projectBean) {
        if (projectBean == null) {
            return null;
        }
        return new Project(
                projectBean.getName(),
                projectBean.getDescription()
        );
    }

    // Convert from Project to ProjectBean
    public static ProjectBean toBean(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectBean(
                project.getName(),
                project.getDescription()
        );
    }
}

