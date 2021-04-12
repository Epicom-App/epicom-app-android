import config.AppBuildConfig.quote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(config.AppAndroidConfig.Sdk.compileVersion)
    defaultConfig {
        applicationId = config.AppAndroidConfig.Application.id
        targetSdkVersion(config.AppAndroidConfig.Sdk.targetVersion)
        minSdkVersion(config.AppAndroidConfig.Sdk.minimalVersion)
        versionCode = versionCode()
        versionName = config.AppAndroidConfig.Application.versionName_displayed
        testInstrumentationRunner = config.AppAndroidConfig.TestRunner.default
        setProperty("archivesBaseName", "$applicationId-$versionCode-v$versionName")
        buildConfigField(
            "String",
            "APPCENTER_SECRET",
            // The APPCENTER_SECRET is defined in a pipeline variable
            quote((project.findProperty("appcenterSecret") as? String) ?: "")
        )
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    signingConfigs {
        getByName(config.AppBuildConfig.Debug.name) {
            storeFile = rootProject.file("config/keystores/debug.keystore.jks")
            keyAlias = "android"
            keyPassword = "android"
            storePassword = "android"
        }
        create(config.AppBuildConfig.Release.name) {
            storeFile = file(project.findProperty("keyStoreLocation") as? String ?: "notset")
            storePassword = project.findProperty("keyStorePassword") as? String ?: "notset"
            keyAlias = project.findProperty("keyAlias") as? String ?: "notset"
            keyPassword = project.findProperty("keyPassword") as? String ?: "notset"
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    buildTypes {
        getByName(config.AppBuildConfig.Debug.name) {
            applicationIdSuffix = ".dev"
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName(config.AppBuildConfig.Debug.name)
            manifestPlaceholders(config.AppBuildConfig.Debug.manifestPlaceholders)
            config.EndpointKey.values().forEach { key ->
                buildConfigField(
                    type = key.type,
                    name = key.name,
                    value = project.findProperty(key.name) as? String ?: quote("Define ${key.name} variable in the debug build pipeline")
                )
            }
        }
        getByName(config.AppBuildConfig.Release.name) {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName(config.AppBuildConfig.Release.name)
            manifestPlaceholders(config.AppBuildConfig.Release.manifestPlaceholders)
            config.EndpointKey.values().forEach { key ->
                buildConfigField(
                    type = key.type,
                    name = key.name,
                    value = project.findProperty(key.name) as? String ?: quote("Define ${key.name} variable in the prod build pipeline")
                )
            }
        }
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }
    lintOptions.isAbortOnError = false
}

dependencies {

    coreLibraryDesugaring(Libs.Desugaring.core)

    // Ui Modules
    implementation(project(Modules.ui_common))
    implementation(project(Modules.ui_main))
    implementation(project(Modules.ui_map))
    implementation(project(Modules.ui_widget))
    implementation(project(Modules.ui_debug))
    implementation(project(Modules.ui_settings))
    implementation(project(":ui:onboarding"))
    implementation(project(Modules.presentation))
    implementation(project(Modules.ui_webview))
    implementation(project(":ui:legend"))

    // Features
    implementation(project(":features:logging"))
    implementation(project(Modules.common_code))
    implementation(project(Modules.geotracking))
    implementation(project(Modules.backgroundUpdates))
    implementation(project(":features:permissions"))
    implementation(project(":features:appcenter"))
    implementation(project(":features:load_regions"))
    implementation(project(":features:settings"))

    // Kotlin & Friends
    implementation(Libs.Kotlin.stdLib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)

    // AndroidX
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Material.core)
    implementation(Libs.Layout.constraint)
    implementation(Libs.AndroidX.Lifecycle.processLifecycle)
    kapt(Libs.AndroidX.Lifecycle.compiler)

    // Koin
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.coreExt)
    implementation(Libs.Koin.android)
    implementation(Libs.Koin.androidxExt)

    // Jet Pack
    implementation(Libs.JetPackNavigation.fragment)
    implementation(Libs.JetPackNavigation.ui)

    // Debug only
    debugImplementation(Libs.CanaryLeak.debugCore)
}

fun versionCode() : Int {
    return (if (project.hasProperty("versionCode")) {
        project.property("versionCode") as String
    } else {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd00"))
    }).toInt()
}