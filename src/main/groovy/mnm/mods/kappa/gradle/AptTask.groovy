package mnm.mods.kappa.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AptTask extends DefaultTask {

    @Input
    def factory = project.configurations[KappaPlugin.CONFIG_PROCESSOR]
    @Input
    def options = project.extensions[KappaPlugin.EXT_PROCESSOR].options

    AptTask() {
        // make sure we need to do this.
        onlyIf { !factory.isEmpty() }
    }

    @TaskAction
    abstract void doTask()

}
