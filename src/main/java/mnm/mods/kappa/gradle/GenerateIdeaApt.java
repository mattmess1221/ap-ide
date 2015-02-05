package mnm.mods.kappa.gradle;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenerateIdeaApt extends AptTask {

    private static final String COMPILER_XML = ".idea/compiler.xml";
    private static final String PROJECT = "project";
    private static final String COMPONENT = "component";
    private static final String ANNOTATION_PROCESSING = "annotationProcessing";
    private static final String PROFILE = "profile";
    private static final String PROCESSOR_PATH = "processorPath";

    @Override
    public void doTask() {
        enableProcessing();
    }

    private void enableProcessing() {
        try {
            File compilerXml = getProject().file(COMPILER_XML);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element profile;
            Document doc = compilerXml.exists() ? builder.parse(compilerXml) : builder
                    .newDocument();
            if (!compilerXml.exists()) {
                // create the barebones file.
                doc = builder.newDocument();
                Element project = doc.createElement(PROJECT);
                Element component = doc.createElement(COMPONENT);
                Element annotationProcessor = doc.createElement(ANNOTATION_PROCESSING);
                profile = doc.createElement(PROFILE);
                profile.setAttribute("default", "true");
                profile.setAttribute("name", "Default");
                profile.setAttribute("enabled", "true");
                Element processorPath = doc.createElement(PROCESSOR_PATH);
                processorPath.setAttribute("useClasspath", "true");

                profile.appendChild(processorPath);
                annotationProcessor.appendChild(profile);
                component.appendChild(annotationProcessor);
                project.appendChild(component);
                doc.appendChild(project);
            } else {
                // parse the xml
                doc = builder.parse(compilerXml);
                profile = (Element) doc.getElementsByTagName(PROFILE).item(0);
            profile.setAttribute("enabled", "true");
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // make pretty
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                    Integer.toString(4));

            // write it
            compilerXml.getParentFile().mkdirs();
            compilerXml.createNewFile();
            Source source = new DOMSource(doc);
            StreamResult result = new StreamResult(compilerXml);
            transformer.transform(source, result);
        } catch (Exception e) {
            throwError(e);
        }
    }
}
