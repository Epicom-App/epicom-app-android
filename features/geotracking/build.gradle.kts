plugins {
    id("com.android.library")
    kotlin("android")
}

// This is for each project module
android {
    compileSdkVersion(config.AppAndroidConfig.Sdk.compileVersion)
    defaultConfig {
        targetSdkVersion(config.AppAndroidConfig.Sdk.targetVersion)
        minSdkVersion(config.AppAndroidConfig.Sdk.minimalVersion)
        versionCode = config.AppAndroidConfig.Application.versionCode_libModules
        versionName = config.AppAndroidConfig.Application.versionName_displayed
        testInstrumentationRunner = config.AppAndroidConfig.TestRunner.default
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {

    coreLibraryDesugaring(Libs.Desugaring.core)

    implementation(project(":features:logging"))
    implementation(project(Modules.common_code))
    implementation(project(":features:permissions"))

    // Kotlin
    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)
    implementation(Libs.Kotlin.Coroutines.playServices)

    // Google Play Services
    implementation(Libs.GooglePlay.location)
    // Koin
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.coreExt)
}