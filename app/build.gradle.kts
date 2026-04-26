plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinCompose)
    id(BuildPlugins.ksp)
    id(BuildPlugins.hilt)
}

android {
    namespace = "dev.lamm.pennydrop"
    compileSdk = AndroidSDK.compile

    defaultConfig {
        applicationId = "dev.lamm.pennydrop"
        minSdk = AndroidSDK.min
        targetSdk = AndroidSDK.target
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        jvmToolchain(21)
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.05.01"))

    implementation(Libraries.JetPack.coreKtx)
    implementation(Libraries.JetPack.constraintLayoutCompose)
    implementation(Libraries.JetPack.lifecycleLivedata)
    implementation(Libraries.JetPack.lifecycleViewmodel)
    implementation(Libraries.JetPack.navigationCompose)
    implementation(Libraries.JetPack.hiltNavigationCompose)
    implementation(Libraries.JetPack.preferenceKtx)
    implementation(Libraries.JetPack.roomKtx)
    implementation(Libraries.JetPack.roomRuntime)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${Versions.Jetpack.LIFECYCLE}")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.9.1")

    implementation(Libraries.Google.hilt)
    implementation(Libraries.Google.material)

    ksp(Libraries.Google.hiltCompiler)
    ksp(Libraries.JetPack.roomCompiler)

    testImplementation(TestLibraries.junit)

    androidTestImplementation(TestLibraries.AndroidXTest.junit)
    androidTestImplementation(TestLibraries.AndroidXTest.espressoCore)
}
