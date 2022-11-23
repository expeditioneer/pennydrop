object BuildPlugins {
    val androidApplication  by lazy { "com.android.application" }
    val androidLibrary by lazy { "com.android.library" }
    val hilt by lazy { "com.google.dagger.hilt.android"}
    val kotlinAndroid by lazy { "org.jetbrains.kotlin.android" }
}

object Libraries {
    object JetPack {
        val appcompat by lazy { "androidx.appcompat:appcompat:1.5.1" }
        val constrainedLayout by lazy { "androidx.constraintlayout:constraintlayout:2.1.4" }
        val coreKtx by lazy { "androidx.core:core-ktx:1.8.0" }
        val hilt by lazy { "com.google.dagger:hilt-android:${Versions.Jetpack.hilt}" }
        val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.Jetpack.hilt}" }
        val lifecycleLivedata by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Jetpack.lifecycle}" }
        val lifecycleViewmodel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Jetpack.lifecycle}" }
        val material by lazy { "com.google.android.material:material:1.7.0" }
        val navigationFragment by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.Jetpack.navigation}" }
        val navigationUiKtx by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.Jetpack.navigation}" }
        val preferenceKtx by lazy { "androidx.preference:preference-ktx:${Versions.Jetpack.preferences}" }
        val recyclerview by lazy { "androidx.recyclerview:recyclerview:${Versions.Jetpack.recyclerview}" }
        val roomKtx by lazy { "androidx.room:room-ktx:${Versions.Jetpack.room}" }
        val roomRuntime by lazy { "androidx.room:room-runtime:${Versions.Jetpack.room}" }
        val roomCompiler by lazy { "androidx.room:room-compiler:${Versions.Jetpack.room}" }
    }
}

object TestLibraries {
    val junit by lazy { "junit:junit:4.13.2" }

    object AndroidXTest {
        val espressoCore by lazy { "androidx.test.espresso:espresso-core:3.4.0" }
        val junit by lazy { "androidx.test.ext:junit:1.1.3" }
    }
}