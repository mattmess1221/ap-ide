package mnm.mods.kappa.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin used to enable annotation processing in Eclipse and IntelliJ.
 * Will automatically add any processors found in the compile configuration.
 * Adds tasks {@code eclipseApt} and {@code ideaApt} to the {@code eclipse} and
 * {@code idea} tasks respectively.
 *
 * @author Matthew Messinger
 */
class KappaPlugin implements Plugin<Project> {

    private static final String PROCESSOR_FILE = 'META-INF/services/javax.annotation.processing.Processor'

    static final String CONFIG_PROCESSOR = 'factory'
    static final String EXT_PROCESSOR = 'processor'

    @Override
    void apply(Project project) {
        project.with {
            configurations.create CONFIG_PROCESSOR
            extensions.create EXT_PROCESSOR, ProcessorExtension

        	if  (plugins.hasPlugin('eclipse')) {
                // add eclipseApt task to eclipse.
                def eclipseApt = tasks.create("eclipseApt", GenerateEclipseApt)
                tasks.eclipse.dependsOn eclipseApt
            }
            if (plugins.hasPlugin('idea')) {
                // add ideaApt task to idea.
                def ideaApt = tasks.create('ideaApt', GenerateIdeaApt)
                tasks.idea.dependsOn ideaApt
            }
        }
    }
}
