package mnm.mods.kappa.gradle;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.StopExecutionException;
import org.gradle.api.tasks.TaskAction;

public abstract class AptTask extends DefaultTask implements Spec<Task> {

    private static final String CONFIG_COMPILE = "compile";
    private static final String PROCESSOR_FILE = "META-INF/services/javax.annotation.processing.Processor";

    private static boolean done;

    public AptTask() {
        // make sure we need to do this.
        this.onlyIf(this);
    }

    @TaskAction
    public abstract void doTask();

    @Override
    public final boolean isSatisfiedBy(Task task) {
        findProcessors();
        Configuration aptCompile = getProject().getConfigurations().getByName(
                KappaPlugin.CONFIG_APT_COMPILE);
        return !aptCompile.isEmpty();
    }

    private void findProcessors() {
        // don't run multiple times.
        if (!done) {
            Configuration configuration =
                    getProject().getConfigurations().getByName(CONFIG_COMPILE);
            // iterate compile configuration
            for (File f : configuration) {
                if (isZipApt(f)) {
                    // add to aptCompile configuration
                    getProject().getDependencies().add(KappaPlugin.CONFIG_APT_COMPILE,
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
                ZipEntry entry = zip.getEntry(PROCESSOR_FILE);
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

    protected void throwError(Throwable e) {
        final String msg = "Unable to enable the IDE's annotation processing";
        getLogger().info(msg, e);
        throw new StopExecutionException(msg);
    }
}
