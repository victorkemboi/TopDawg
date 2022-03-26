plugins {
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

buildscript {

    val kotlinVersion = "1.6.10"
//    val jacocoVersion = "0.8.7"

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://plugins.gradle.org/m2/")) // For kotlinter-gradle
    }

    dependencies {
        // keeping this here to allow AS to automatically update
        classpath("com.android.tools.build:gradle:7.3.0-alpha07")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.3")
//            classpath(shadow)
        classpath("org.jmailen.gradle:kotlinter-gradle:3.4.5")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.39.0")
        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:0.11.1-new-mm")
//        classpath("org.jacoco:org.jacoco.core:$jacocoVersion")
        classpath("dev.icerock.moko:resources-generator:0.19.0")
    }
}

allprojects {
    apply(plugin = "org.jmailen.kotlinter")

    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
    }
}


// On Apple Silicon we need Node.js 16.0.0
// https://youtrack.jetbrains.com/issue/KT-49109
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class) {
    rootProject.the(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension::class).nodeVersion =
        "16.0.0"
}