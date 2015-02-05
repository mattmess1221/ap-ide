package mnm.mods.kappa.gradle;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateEclipseApt extends AptTask {

    @Override
    public void execute(Task task) {
        enableAnnotations();
        createAptPrefs();
        createFactorypath();
    }

    private void enableAnnotations() {
        // modify the jdt properties file to enable processing
        EclipseModel eclipse = (EclipseModel) getProject().getExtensions().getByName("eclipse");
        eclipse.getJdt().getFile().getTransformer().addAction(new Action<Properties>() {
            @Override
            public void execute(Properties properties) {
                properties.setProperty(Constants.ECLIPSE_PROCESS_ANNOTATIONS, "enabled");
            }
        });
    }

    private void createAptPrefs() {
        // create the apt properties file.
        // TODO don't replace if exists. May remove commons-io dependency
        try {
            File file = getProject().file(Constants.ECLIPSE_APT_PREFS_FILE);
            FileUtils.writeStringToFile(file, Constants.ECLIPSE_APT_PREFS_CONTENTS);
        } catch (IOException e) {
            throwError(e);
        }
    }

    private void createFactorypath() {
        // Creates the .factorypath xml file used to define annotation
        // processors.
        try {
            // factorypath
            Configuration gen =
                    getProject().getConfigurations().getByName(Constants.CONFIG_APT_COMPILE);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element factorypath = doc.createElement(Constants.ECLIPSE_FACTORYPATH);
            for (File f : gen) {
                Element entry = doc.createElement(Constants.ECLIPSE_FACTORYPATH_ENTRY);
                entry.setAttribute("kind", "EXTJAR");
                entry.setAttribute("id", f.getAbsolutePath());
                entry.setAttribute("enabled", "true");
                entry.setAttribute("runInBatchMode", "false");
                factorypath.appendChild(entry);
            }
            doc.appendChild(factorypath);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            // make pretty
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                    Integer.toString(4));
            // write it
            Source source = new DOMSource(doc);
            StreamResult result =
                    new StreamResult(getProject().file(Constants.ECLIPSE_FACTORYPATH_FILE));
            transformer.transform(source, result);
        } catch (Exception e) {
            throwError(e);
        }
    }

    private void throwError(Throwable t) {
        throw new GradleException("Build failed while enabling Eclipse Apt", t);
    }
}
