###Kappa - Gradle
This is a plugin for Gradle that enables Eclipse APT
 and automatically adds annotation processors from
 the classpath. To use, put this in your builscript.
```
buildscript {
    repositories.mavenCentral()
    dependencies {
        classpath files("path/to/Kappa-gradle-1.0.jar")
        classpath "commons-io:commons-io:2.1"
    }
}
apply plugin: 'kappa'

dependencies {
    compile files("path/to/Kappa-core-1.0.jar")
}
```