package mnm.mods.kappa.gradle

import groovy.transform.PackageScope

import java.util.zip.ZipFile

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

    static final String CONFIG_FACTORY = 'factory'

    @Override
    void apply(Project project) {
        project.with {
            apply plugin: 'eclipse'
            apply plugin: 'idea'

            configurations.create CONFIG_FACTORY

            // add eclipseApt task to eclipse.
            def eclipseApt = tasks.create "eclipseApt", GenerateEclipseApt
            tasks['eclipse'].dependsOn eclipseApt

            // add ideaApt task to idea.
            def ideaApt = tasks.create 'ideaApt', GenerateIdeaApt.class
            tasks['idea'].dependsOn ideaApt
        }
    }

    @PackageScope
    static boolean isProcessor(File f) {
        // Tests if a file is a processor jar.
        if (f.file) {
            ZipFile zip = null
            try {
                zip = new ZipFile(f)
                def entry = zip.getEntry PROCESSOR_FILE
                entry != null
            } catch (IOException e) {
                // quietly ignore
            } finally {
                closeQuietly(zip)
            }
        } else {
            false
        }
    }

    private static void closeQuietly(Closeable close) {
        try {
            close?.close()
        }catch(IOException e) {
            // ignore
        }
    }
}
