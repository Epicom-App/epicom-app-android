import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import config.AppAndroidConfig

plugins {
    id("com.android.library")
    kotlin("android")
    //id("de.af.resgen")
}

// TODO: Commented out because af repository is insecure (http) and the new gradle rejects that

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
    packagingOptions{
        exclude("META-INF/*.kotlin_module")
    }
    buildFeatures.viewBinding = true
}

dependencies {

    implementation(Libs.Kotlin.Coroutines.core)

    // AndroidX
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.lifeCycleCommon)

    api(Libs.Material.core)
}

//resGen {
//
//    fileId = getGradleLocalProperty("resFileGoogleDriveId") as? String ?: "notset"
//
//    strings(closureOf<de.appsfactory.ResGenExtension.Config> {
//        keysColumn = "B"
//        languageRow = "2"
//        languageStartColumn = "C"
//        languageEndColumn = "D"
//        defaultLanguage = "en"
//        placeHolderFile = "../../config/stringplaceholders/stringPlaceHolders.txt"
//    })
//
//
//    colors(closureOf<de.appsfactory.ResGenExtension.Config> {
//        keysColumn = "B"
//        languageStartColumn = "E"
//        languageEndColumn = "E"
//    })
//
//
//    nonLocalizedStrings(closureOf<de.appsfactory.ResGenExtension.Config> {})
//    tracking(closureOf<de.appsfactory.ResGenExtension.Config> {})
//
//}


// Define local variables in local.properties
fun getGradleLocalProperty(key: String): String =
    gradleLocalProperties(rootDir).getProperty(key) ?: "$key notset"