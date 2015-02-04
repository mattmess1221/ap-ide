package mnm.mods.kappa.gradle;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskAction;

public class FindProcessorsTask extends DefaultTask implements Constants {

    @TaskAction
    public void doTask() {

        Configuration configuration = getProject().getConfigurations().getByName(
                Constants.CONFIG_COMPILE);
        for (File f : configuration) {
            if (isZipApt(f)) {
                getProject().getDependencies().add(Constants.CONFIG_APT_COMPILE,
                        getProject().files(f));
            }
        }
    }

    private boolean isZipApt(File f) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(f);
            ZipEntry entry = zip.getEntry(Constants.PROCESSOR_FILE);
            return (entry != null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(zip);
        }
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
