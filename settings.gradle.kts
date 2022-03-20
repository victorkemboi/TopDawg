pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "TopDawg"
include(":androidApp")
include(":common")

includeBuild("gradlePlugins")