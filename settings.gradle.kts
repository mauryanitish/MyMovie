import org.gradle.api.initialization.resolve.RepositoriesMode.*

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url =uri("https://jitpack.io") }  // <--
        jcenter() // Warning: this repository is going to shut down soon
    }
}

rootProject.name = "My Movie"
include(":app")
