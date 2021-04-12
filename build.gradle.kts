import GradleTasks.ANALYZE_CODE_TASK
import GradleTasks.CLEAN_TASK
import GradleTasks.DETEKT_AUTO_CORRECT_TASK
import GradleTasks.DETEKT_TASK
import com.android.build.gradle.tasks.LintGlobalTask

buildscript {
    repositories {
        google()
        jcenter()
        //maven(url = "http://s2.appsfactory.de/APPSfactory/Maven" )
        maven(url = uri("https://plugins.gradle.org/m2/"))
    }
    dependencies {
        classpath(GradleBuildPlugins.gradle)
        classpath(GradleBuildPlugins.kotlin)
        classpath(GradleBuildPlugins.Jacoco.classPath)
        //classpath(GradleBuildPlugins.afResGen)
        classpath(GradleBuildPlugins.navigation)
        classpath(GradleBuildPlugins.appBadge)
        classpath(GradleBuildPlugins.firebase)
        classpath(GradleBuildPlugins.koin)
        classpath(GradleBuildPlugins.kotlinSerialization)
        classpath(GradleBuildPlugins.sqlDelight)
        classpath(GradleBuildPlugins.kotlin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/kotlin/kotlinx")
        maven(url = "https://jitpack.io")
//        maven(url = "http://s2.appsfactory.de/APPSfactory/Maven")
//        maven(url = "http://oss.jfrog.org/artifactory/oss-snapshot-local")
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }
}

plugins {
    id(GradleBuildPlugins.Jacoco.plugin) version GradleBuildPlugins.Jacoco.version
    id(GradleBuildPlugins.Detekt.plugin) version GradleBuildPlugins.Detekt.version
    kotlin("jvm") version "1.4.32"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

junitJacoco {
    jacocoVersion = "0.8.5"
    ignoreProjects
    excludes
    includeNoLocationClasses = false
    includeInstrumentationCoverageInMergedReport = false
}

detekt {
    config.setFrom(files("$projectDir/config/detekt/config.yml"))
    buildUponDefaultConfig = true
    parallel = true
    reports {
        html.enabled = false
        xml.enabled = true
        txt.enabled = false
    }
}

subprojects {

    apply(plugin = GradleBuildPlugins.Detekt.plugin)

    detekt { ignoreFailures = true }
    
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class)
        .configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }

    tasks.withType(LintGlobalTask::class.java, closureOf<LintGlobalTask> {
        tasks.findByName(ANALYZE_CODE_TASK)?.dependsOn(
            this, tasks.named(DETEKT_TASK)
        )
    })
}

task<DefaultTask>(name = ANALYZE_CODE_TASK) {
    group = "verification"
    description = "Runs code analyzers"
}

task<io.gitlab.arturbosch.detekt.Detekt>(name = DETEKT_AUTO_CORRECT_TASK) {
    description = "Runs a fail fast detekt build."
    config.setFrom(files("$projectDir/config/detekt/config.yml"))
    source = fileTree(projectDir)
    debug = true
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    ignoreFailures = true
    reports {
        html.enabled = true
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
}
