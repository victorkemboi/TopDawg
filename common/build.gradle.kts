import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import com.mes.topdawg.DependenciesPlugin.Deps
import com.mes.topdawg.DependenciesPlugin.Versions


plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
    id("com.mes.topdawg.dependencies")
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

android {
    compileSdk = Versions.androidCompileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    val iosTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
        when {
            System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
            System.getenv("NATIVE_ARCH")
                ?.startsWith("arm") == true -> ::iosSimulatorArm64 // available to KT 1.5.30
            else -> ::iosX64
        }
    iosTarget("iOS") {}

    val sdkName: String? = System.getenv("SDK_NAME")
    val isWatchOSDevice = sdkName.orEmpty().startsWith("watchos")
    if (isWatchOSDevice) {
        watchosArm64("watch")
    } else {
        watchosX64("watch")
    }

    macosX64("macOS")
    android()
    jvm()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "common"
        }
    }

    js(IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        sourceSets["commonMain"].dependencies {

            with(Deps.Ktor) {
                implementation(clientCore)
                implementation(clientJson)
                implementation(clientLogging)
                implementation(contentNegotiation)
                implementation(json)
            }

            with(Deps.Kotlinx) {
                implementation(coroutinesCore)
                implementation(serializationCore)
            }

            with(Deps.SqlDelight) {
                implementation(runtime)
                implementation(coroutineExtensions)
            }

            with(Deps.Koin) {
                api(core)
                api(test)
            }

            with(Deps.Log) {
                api(kermit)
            }
        }
        sourceSets["commonTest"].dependencies {
            implementation(Deps.Koin.test)
            implementation(Deps.Kotlinx.coroutinesTest)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        sourceSets["androidMain"].dependencies {
            implementation(Deps.Ktor.clientAndroid)
            implementation(Deps.SqlDelight.androidDriver)
        }
        sourceSets["androidTest"].dependencies {
            implementation(Deps.Test.junit)
        }

        sourceSets["jvmMain"].dependencies {
            implementation(Deps.Ktor.clientJava)
            implementation(Deps.SqlDelight.sqliteDriver)
            implementation(Deps.Log.slf4j)
        }

        sourceSets["iOSMain"].dependencies {
            implementation(Deps.Ktor.clientIos)
            implementation(Deps.SqlDelight.nativeDriver)
        }
        sourceSets["iOSTest"].dependencies {
        }

//        sourceSets["watchMain"].dependencies {
//            implementation(Deps.Ktor.clientIos)
//            implementation(Deps.SqlDelight.nativeDriver)
//        }

//        sourceSets["macOSMain"].dependencies {
//            implementation(Deps.Ktor.clientIos)
//            implementation(Deps.SqlDelight.nativeDriverMacos)
//        }
//
//        sourceSets["jsMain"].dependencies {
//            implementation(Deps.Ktor.clientJs)
//        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

sqldelight {
    database("TopDawgDatabase") {
        packageName = "com.mes.topdawg.topdawg.db"
        sourceFolders = listOf("sqldelight")
    }
}

multiplatformSwiftPackage {
    packageName("TopDawg")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
    }
}