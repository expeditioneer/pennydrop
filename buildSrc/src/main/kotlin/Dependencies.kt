object BuildPlugins {
    val androidApplication by lazy { "com.android.application" }
    val androidLibrary by lazy { "com.android.library" }
    val cyclonedx by lazy { "org.cyclonedx.bom" }
    val detekt by lazy { "dev.detekt" }
    val hilt by lazy { "com.google.dagger.hilt.android" }
    val kotlinAndroid by lazy { "org.jetbrains.kotlin.android" }
    val kotlinCompose by lazy { "org.jetbrains.kotlin.plugin.compose" }
    val ksp by lazy { "com.google.devtools.ksp" }
}

object Libraries {
    object JetPack {
        val appcompat by lazy { "androidx.appcompat:appcompat:${Versions.Jetpack.APP_COMPAT}" }
        val constraintLayoutCompose by lazy { "androidx.constraintlayout:constraintlayout-compose:${Versions.Jetpack.CONSTRAINED_LAYOUT_COMPOSE}" }
        val coreKtx by lazy { "androidx.core:core-ktx:${Versions.Jetpack.CORE}" }
        val lifecycleLivedata by lazy { "androidx.lifecycle:lifecycle-livedata:${Versions.Jetpack.LIFECYCLE}" }
        val lifecycleViewmodel by lazy { "androidx.lifecycle:lifecycle-viewmodel:${Versions.Jetpack.LIFECYCLE}" }
        val navigationCompose by lazy { "androidx.navigation:navigation-compose:${Versions.Jetpack.NAVIGATION}" }
        val hiltNavigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.Jetpack.HILT_NAV_COMPOSE}" }
        val preferenceKtx by lazy { "androidx.preference:preference-ktx:${Versions.Jetpack.PREFERENCES}" }
        val roomKtx by lazy { "androidx.room:room-ktx:${Versions.Jetpack.ROOM}" }
        val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.Jetpack.ROOM}" }
        val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.Jetpack.ROOM}" }
    }

    object Google {
        val hilt by lazy { "com.google.dagger:hilt-android:${Versions.Google.HILT}" }
        val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.Google.HILT}" }
        val material by lazy { "com.google.android.material:material:${Versions.Google.MATERIAL}" }
    }
}

object TestLibraries {
    val junit by lazy { "junit:junit:4.13.2" }

    object AndroidXTest {
        val espressoCore by lazy { "androidx.test.espresso:espresso-core:3.7.0" }
        val junit by lazy { "androidx.test.ext:junit:1.3.0" }
    }
}