import config.AppAndroidConfig

plugins {
    id("com.android.library")
    kotlin("android")
}

// This is for each project module
android {
    compileSdkVersion(AppAndroidConfig.Sdk.compileVersion)
    defaultConfig {
        targetSdkVersion(AppAndroidConfig.Sdk.targetVersion)
        minSdkVersion(AppAndroidConfig.Sdk.minimalVersion)
        versionCode = AppAndroidConfig.Application.versionCode_libModules
        versionName = AppAndroidConfig.Application.versionName_displayed
        testInstrumentationRunner = AppAndroidConfig.TestRunner.default
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    resourcePrefix("ui_onboarding_")
    buildFeatures.viewBinding = true
}

dependencies {

    // App Modules
    implementation(project(Modules.common_code))
    implementation(project(Modules.presentation))
    implementation(project(Modules.ui_common))
    implementation(project(":ui:webview"))
    implementation(project(":features:logging"))
    implementation(project(":ui:legend"))

    // AndroidX
    implementation(Libs.Material.core)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.viewModel)
    implementation(Libs.AndroidX.cardView)
    implementation(Libs.Layout.constraint)

    // Koin
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.coreExt)
    implementation(Libs.Koin.android)
    implementation(Libs.Koin.androidxExt)
    implementation(Libs.Koin.androidxFragment)
    implementation(Libs.Koin.androidxViewModel)
    implementation(Libs.Koin.androidxViewModelScope)


    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Jetpack Navigation
    implementation(Libs.JetPackNavigation.ui)
    implementation(Libs.JetPackNavigation.fragment)

    // Flow Bindings
    implementation(Libs.FlowBindings.material)

}