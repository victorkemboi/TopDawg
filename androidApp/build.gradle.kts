plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.mes.topdawg.android"
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

    implementation(libs.accompanist.navigation.animation)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraint.layout.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.compose.compiler)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.tooling)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material)
    implementation(libs.compose.navigation)
    implementation(libs.coil.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    testImplementation(libs.koin.junit4)
    testImplementation(libs.koin.junit5)
    androidTestImplementation(libs.koin.junit4)
    androidTestImplementation(libs.koin.junit5)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.json)

    implementation(libs.material.android)
}