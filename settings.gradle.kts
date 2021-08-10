pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:4.0.1")
            }
        }
    }
}
rootProject.name = "EbolaApp"

include(":app",":ui",":presentation",":features")

// Ui
include(
    ":ui:common",
    ":ui:main",
    ":ui:navigation",
    ":ui:map",
    ":ui:widget",
    ":ui:debug",
    ":ui:settings",
    ":ui:tableview",
    ":ui:webview",
    ":ui:onboarding",
    ":ui:legend"
)
// Features
include(
    ":features:geotracking",
    ":features:background_updates",
    ":features:permissions",
    ":features:logging",
    ":features:load_regions",
    ":features:appcenter",
    ":features:settings",
    ":maps:proxy",
    ":maps:google",
    ":maps:huawei",
    ":location:proxy",
    ":location:google",
    ":location:huawei"
)

include(":common:common_code")
project(":common:common_code").projectDir = file("../common/common_code")

