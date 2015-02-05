package mnm.mods.kappa.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Gradle plugin used to enable annotation processing in Eclipse and IntelliJ.
 * Will automatically add any processors found in the compile configuration.
 * Adds tasks {@code eclipseApt} and {@code ideaApt} to the {@code eclipse} and
 * {@code idea} tasks respectively.
 *
 * @author Matthew Messinger
 *
 */
public class KappaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply("eclipse");
        project.getPlugins().apply("idea");
        project.getConfigurations().create(Constants.CONFIG_APT_COMPILE);

        // add eclipseApt task to eclipse.
        Task eclipseApt = project.getTasks().create("eclipseApt", GenerateEclipseApt.class);
        project.getTasks().getByName("eclipse").dependsOn(eclipseApt);

        // add ideaApt task to idea.
        Task ideaApt = project.getTasks().create("ideaApt", GenerateIdeaApt.class);
        project.getTasks().getByName("idea").dependsOn(ideaApt);
    }
}
