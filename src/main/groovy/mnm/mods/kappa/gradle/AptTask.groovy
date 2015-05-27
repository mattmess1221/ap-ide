package mnm.mods.kappa.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AptTask extends DefaultTask {

    @Input
    protected Configuration factory = project.configurations[KappaPlugin.CONFIG_FACTORY]

    AptTask() {
        // make sure we need to do this.
        onlyIf { !factory.isEmpty() }
    }

    @TaskAction
    abstract void doTask()

}
