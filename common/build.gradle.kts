import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

// CocoaPods requires the podspec to have a version.
version = "1.0"

android {
    compileSdk = 31
    namespace = "com.mes.topdawg.android"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
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

//    val sdkName: String? = System.getenv("SDK_NAME")
//    val isWatchOSDevice = sdkName.orEmpty().startsWith("watchos")
//    if (isWatchOSDevice) {
//        watchosArm64("watch")
//    } else {
//        watchosX64("watch")
//    }

//    macosX64("macOS")
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

//    js(IR) {
//        useCommonJs()
//        browser()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(libs.kermit)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.okio)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines.ext)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.coroutines.test)
                implementation(libs.koin.test)
                implementation(libs.okio.fake.file.system)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.android)
            }
        }

        val androidTest by getting {
            dependencies {
                // Todo: Setup junit tests
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.sqlite)
            }
        }

        val iOSMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.native)
            }
        }
        val iOSTest by getting {
            dependencies {}
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

tasks.withType<KotlinCompile> {
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