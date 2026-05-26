plugins {
    id(BuildPlugins.androidApplication) version Versions.BUILD_TOOLS apply false
    id(BuildPlugins.androidLibrary) version Versions.BUILD_TOOLS apply false
    id(BuildPlugins.kotlinAndroid) version Versions.KOTLIN apply false
    id(BuildPlugins.kotlinCompose) version Versions.KOTLIN apply false
    id(BuildPlugins.ksp) version Versions.Google.KSP apply false
    id(BuildPlugins.hilt) version Versions.Google.HILT apply false
    id(BuildPlugins.detekt) version Versions.DETEKT apply false
    id(BuildPlugins.cyclonedx) version Versions.CYCLONEDX apply false
}
