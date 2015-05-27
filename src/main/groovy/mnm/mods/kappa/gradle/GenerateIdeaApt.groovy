package mnm.mods.kappa.gradle

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import org.gradle.api.tasks.OutputFile

class GenerateIdeaApt extends AptTask {

    private static final String COMPILER_XML = '.idea/compiler.xml'
    private static final String PROJECT = 'project'
    private static final String COMPONENT = 'component'
    private static final String ANNOTATION_PROCESSING = 'annotationProcessing'
    private static final String PROFILE = 'profile'
    private static final String PROCESSOR_PATH = 'processorPath'

    @OutputFile
    private File compilerXml = project.rootProject.file COMPILER_XML

    @Override
    void doTask() {
        enableProcessing()
    }

    private void enableProcessing() {
        if (compilerXml.exists() && compilerXml.size() > 0) {
            // save the current xml
            def xml = new XmlSlurper().parse compilerXml
            def compiler =  xml."$COMPONENT".find {it.@name == 'CompilerConfiguration'}
            def anno = compiler."$ANNOTATION_PROCESSING"
            if (anno != null && !anno.isEmpty()) {
                anno[0]."$PROFILE".find {it.@name = 'Default'}?.@enabled = 'true'
            } else {
                compiler.appendNode {
                    "$ANNOTATION_PROCESSING" {
                        "$PROFILE"('default':'true',name:'Default',enabled:'true') {
                            "$PROCESSOR_PATH"(useAttribute:'true')
                        }
                    }
                }
            }

            compilerXml.withWriter {
                XmlUtil.serialize new StreamingMarkupBuilder().bind { mkp.yield xml }, it
            }
        } else  {
            compilerXml.parentFile.mkdirs()
            compilerXml.createNewFile()

            def xml = new MarkupBuilder(new FileWriter(compilerXml))

            def annotation = ANNOTATION_PROCESSING
            def profile = PROFILE
            def processorPath = PROCESSOR_PATH

            xml."$PROJECT" {
                "$COMPONENT"(name: 'CompilerConfiguration') {
                    "$annotation" {
                        "$profile"(
                                'default': 'true',
                                name: 'Default',
                                enabled: 'true') {
                                    "$processorPath"(useAttribute: 'true')
                                }
                    }
                }
            }
        }
    }
}
