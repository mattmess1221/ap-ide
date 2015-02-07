###Kappa - Gradle
This is a plugin for Gradle that enables Eclipse APT
 and automatically adds annotation processors from
 the classpath. To use, put this in your builscript.
```
buildscript {
    dependencies {
        classpath files("path/to/Kappa-gradle-1.0.jar")
    }
}
apply plugin: 'kappa'

dependencies {
    compile files("path/to/Kappa-core-1.0.jar")
}
```
It automatically adds eclipse and idea, so there is
 no need to apply those.