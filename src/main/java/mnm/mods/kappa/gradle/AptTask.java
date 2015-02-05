package mnm.mods.kappa.gradle;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskAction;

public abstract class AptTask extends DefaultTask implements Action<Task> {

    private static boolean done;

    public AptTask() {
        this.doLast(this);
    }

    @TaskAction
    public final void doTask() {
        // don't run multiple times.
        if (!done) {
            Configuration configuration =
                    getProject().getConfigurations().getByName(Constants.CONFIG_COMPILE);
            // iterate compile configuration
            for (File f : configuration) {
                if (isZipApt(f)) {
                    // add to aptCompile configuration
                    getProject().getDependencies().add(Constants.CONFIG_APT_COMPILE,
                            getProject().files(f));
                }
            }
            done = true;
        }
    }

    private boolean isZipApt(File f) {
        // Tests if a file is a processor jar.
        if (f.isFile()) {
            ZipFile zip = null;
            try {
                zip = new ZipFile(f);
                ZipEntry entry = zip.getEntry(Constants.PROCESSOR_FILE);
                return (entry != null);
            } catch (IOException e) {
                // quietly ignore
            } finally {
                closeQuietly(zip);
            }
        }
        return false;
    }

    private void closeQuietly(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
