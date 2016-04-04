package mnm.mods.kappa.gradle

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import org.gradle.api.tasks.OutputFile

class GenerateIdeaApt extends AptTask {

    private static final String COMPILER_XML = '.idea/compiler.xml'

    @OutputFile
    private File compilerXml = project.file COMPILER_XML

    @Override
    void doTask() {
        enableProcessing()
    }

    private void enableProcessing() {
        def node = {
            profile('default': 'true', name: 'Default', enabled: 'true') {
                options.each { key, val ->
                    option(name: key, value: val)
                }
                processorPath(useClasspath: 'false') {
                    factory.each { File f ->
                        entry(name: f.absolutePath)
                    }
                }
            }
        }
        if (compilerXml.exists() && compilerXml.size() > 0) {
            // save the current xml
            def xml = new XmlSlurper().parse compilerXml
            def compiler =  xml.component.find {it.'@name' == 'CompilerConfiguration'}
            if (compiler.annotationProcessing != null)
                compiler.annotationProcessing.replaceNode {
                    annotationProcessing node
                }
            else
                compiler.annotationProcessing node
            compilerXml.withWriter {
                XmlUtil.serialize new StreamingMarkupBuilder().bind { mkp.yield xml }, it
            }
        } else  {
            compilerXml.parentFile.mkdirs()
            compilerXml.createNewFile()

            def xml = new MarkupBuilder(new FileWriter(compilerXml))

            xml.project {
                component(name: 'CompilerConfiguration') {
                    annotationProcessing node
                }
            }
        }
    }
}
