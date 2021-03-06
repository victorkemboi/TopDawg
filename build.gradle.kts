plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.kover)
    id("org.jmailen.kotlinter") version "3.10.0" apply false
    alias(libs.plugins.detekt)
}

buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://plugins.gradle.org/m2/")) // For kotlinter-gradle
    }

    dependencies {
        classpath(libs.gradlePlugin.android.gradle)
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.kotlin.serialization)
        classpath(libs.gradlePlugin.sqldelight)
        classpath(libs.gradlePlugin.kotlinter)
        classpath(libs.gradlePlugin.native.coroutines)
        classpath(libs.gradlePlugin.moko.resource.generator)
    }
}

allprojects {
//    apply(plugin = "org.jmailen.kotlinter")
//
//    kotlinter {
//        ignoreFailures = false
//        reporters = arrayOf("checkstyle", "plain")
//        experimentalRules = true
//        disabledRules = arrayOf("no-wildcard-imports")
//    }

//    tasks.li {
//        exclude("./common/build/generated/**/*.kt")
//    }

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