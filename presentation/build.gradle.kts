plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization")
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

    implementation(project(Modules.common_code))
    implementation(project(Modules.geotracking))
    implementation(project(":features:permissions"))
    implementation(project(":features:logging"))
    implementation(project(":features:load_regions"))
    implementation(project(":features:settings"))
    implementation(project(":ui:tableview"))
    implementation(project(Modules.ui_common))

    // Kotlin
    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)
    // Serialization
    implementation(Libs.JsonParser.kotlinSerializationCore)
    implementation(Libs.JsonParser.kotlinSerializationJson)
    // AndroidX
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    // View Model Saved State
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.Lifecycle.viewModelsSavedState)
    implementation(Libs.AndroidX.Lifecycle.liveData)
    implementation(Libs.AndroidX.Lifecycle.lifeCycle)
    // App compat
    implementation(Libs.AndroidX.appCompat)
    // Koin
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.coreExt)
    implementation(Libs.Koin.android)
    implementation(Libs.Koin.androidxFragment)
    implementation(Libs.Koin.androidxViewModel)
    implementation(Libs.Koin.androidxViewModelScope)
    implementation(Libs.Koin.androidxFragment)
    implementation(Libs.Koin.androidxExt)
}