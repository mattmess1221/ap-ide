###ap-ide
This is a plugin for Gradle that enables annotation
 processing in Eclipse and IntelliJ and automatically
 adds annotation processors from the classpath. To
 use, put this in your builscript.
```
plugins {
	id 'mnm.gradle.ap-ide' version '1.0.4'
}
```

Next, tell the plugin what artifacts contain annotation processors
 using the `factory` configuration. 
```groovy
dependencies {
    factory 'org.spongepowered:mixin:0.5.3-SNAPSHOT'
}
```

To tell the ide what options to use for your processor, use the
 `processor` extension and the `options` property.
 
This example uses forge gradle to get srg files for mixin
```groovy
processor {
    options.reobfSrgFile = project.tasks.geSrgs.mcpToSrg.path
}
```
