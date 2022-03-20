import com.mes.topdawg.DependenciesPlugin.Deps
import com.mes.topdawg.DependenciesPlugin.Versions

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.ben-manes.versions")
    id("com.mes.topdawg.dependencies")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.mes.topdawg.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":common"))

    with(Deps.Android) {
        implementation(material)
    }

    with(Deps.AndroidX) {
        implementation(lifecycleRuntimeKtx)
        implementation(lifecycleViewmodelKtx)
        implementation(activityCompose)
    }

    with(Deps.Glance) {
        implementation(appwidget)
    }

    with(Deps.Compose) {
        implementation(compiler)
        implementation(ui)
        implementation(uiGraphics)
        implementation(foundationLayout)
        implementation(material)
        implementation(navigation)
        implementation(coilCompose)
        implementation(accompanistNavigationAnimation)
        implementation(uiTooling)
    }

    with(Deps.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
        testImplementation(test)
        testImplementation(testJUnit4)
    }

    with(Deps.Test) {
        testImplementation(junit)
        androidTestImplementation(androidXTestJUnit)
        testImplementation(testCore)
        testImplementation(robolectric)
        testImplementation(mockito)

        // Compose testing dependencies
        androidTestImplementation(composeUiTest)
        androidTestImplementation(composeUiTestJUnit)
        debugImplementation(composeUiTestManifest)
    }
}