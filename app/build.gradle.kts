@file:Suppress("UnstableApiUsage")

import java.util.Properties


plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.hero.recipespace"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.hero.recipespace"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = Dependencies.Test.ANDROID_JUNIT_RUNNER
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "geminiKey", localProperties.getProperty("geminiKey"))
    }

    buildTypes {
        getByName("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
            manifestPlaceholders["enableCrashlytics"] = "false"
            extra.set("alwaysUpdateBuildId", false)
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            manifestPlaceholders["enableCrashlytics"] = "true"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(Dependencies.Jetpack.androidx_core)
    implementation(Dependencies.Jetpack.appCompat)
    implementation(Dependencies.Libraries.material)
    implementation(Dependencies.Jetpack.constraintLayout)
    implementation(Dependencies.Jetpack.activity)
    implementation(Dependencies.Jetpack.fragment)
    implementation(Dependencies.Jetpack.recyclerView)
    implementation(Dependencies.Jetpack.annotation)
    implementation(Dependencies.Jetpack.swipeRefreshLayout)

    implementation(Dependencies.Libraries.glide)
    ksp(Dependencies.Libraries.glide_compiler)

    implementation(Dependencies.Jetpack.camera_core)
    implementation(Dependencies.Jetpack.camera_camera2)
    implementation(Dependencies.Jetpack.camera_lifecycle)
    implementation(Dependencies.Jetpack.camera_video)
    implementation(Dependencies.Jetpack.camera_view)
    implementation(Dependencies.Jetpack.camera_extensions)

    implementation(Dependencies.Libraries.gson)
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.test_junit)
    androidTestImplementation(Dependencies.Test.espresso_core)

    implementation(platform(Dependencies.Firebase.firebase_bom))
    implementation(Dependencies.Firebase.firebase_fireStore)
    implementation(Dependencies.Firebase.firebase_analytics)
    implementation(Dependencies.Firebase.firebase_auth)
    implementation(Dependencies.Firebase.firebase_storage)
    implementation(Dependencies.Firebase.firebase_messaging)
    implementation(Dependencies.Firebase.firebase_crashlytics)
    implementation(Dependencies.Firebase.firebase_ui_storage)

    implementation(Dependencies.Libraries.play_asset_delivery)
    implementation(Dependencies.Libraries.play_feature_delivery)
    implementation(Dependencies.Libraries.play_review)
    implementation(Dependencies.Libraries.play_app_update)

    implementation(Dependencies.Kotlin.coroutines_core)
    implementation(Dependencies.Kotlin.coroutines_android)
    implementation(Dependencies.Kotlin.coroutines_test)

    implementation(Dependencies.Libraries.elastic_view)

    implementation(Dependencies.Libraries.floating_action_button_speed_dial)

    implementation(Dependencies.Libraries.GEMINI_AI)

    implementation(Dependencies.Jetpack.navigation_fragment)
    implementation(Dependencies.Jetpack.navigation_ui)
    implementation(Dependencies.Jetpack.navigation_dynamic_features_fragment)
    implementation(Dependencies.Jetpack.navigation_testing)

    implementation(Dependencies.Jetpack.hilt_android)
    ksp(Dependencies.Jetpack.hilt_compiler)

    implementation(Dependencies.Jetpack.lifecycle_viewmodel)
    implementation(Dependencies.Jetpack.lifecycle_livedata)
    implementation(Dependencies.Jetpack.lifecycle_viewmodel_savedState)

    implementation(Dependencies.Jetpack.room)
    implementation(Dependencies.Jetpack.room_runtime)
    ksp(Dependencies.Jetpack.room_compiler)
    implementation(Dependencies.Jetpack.room_paging)
    implementation(Dependencies.Jetpack.room_testing)

    implementation(Dependencies.Libraries.preference)

    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.Kotlin.ksp)
}

kapt {
    correctErrorTypes = true
}
