plugins {
    id(BuildPlugins.androidApplication) version Versions.buildTools apply false
    id(BuildPlugins.androidLibrary) version Versions.buildTools apply false
    id(BuildPlugins.kotlinAndroid) version Versions.kotlin apply false
    id(BuildPlugins.hilt) version Versions.Jetpack.hilt apply false
}
