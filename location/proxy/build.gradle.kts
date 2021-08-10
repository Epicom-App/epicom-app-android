plugins {
    id("com.android.library")
    kotlin("android")
}

ext {
    val group_id = "com.proxy.location"
    val artifact_id = "core"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        //kotlinOptions.freeCompilerArgs += ['-module-name', "$group_id.$artifact_id"]
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.Kotlin.stdLib)
}
