package mnm.mods.kappa.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class KappaPlugin implements Plugin<Project> {

    Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        project.getPlugins().apply("eclipse");
        project.getPlugins().apply("idea");
        project.getConfigurations().create(Constants.CONFIG_APT_COMPILE);

        Task find = project.getTasks().create("findProcessors", FindProcessorsTask.class);

        Task eclipseApt = project.getTasks().create("eclipseApt", GenerateEclipseApt.class);
        eclipseApt.dependsOn(find);
        project.getTasks().getByName("eclipse").dependsOn(eclipseApt);

        Task ideaApt = project.getTasks().create("ideaApt", IdeaApt.class);
        ideaApt.dependsOn(find);
        project.getTasks().getByName("idea").dependsOn(ideaApt);


    }
}
