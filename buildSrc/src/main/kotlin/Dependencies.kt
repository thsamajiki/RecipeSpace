object Dependencies {

    object Versions {
        const val kotlin = "1.9.20"
        const val agp = "8.0.2"
        const val ksp = "1.9.20-1.0.14"
        const val hilt = "2.51"
        const val jvmTarget = "17"
        const val material = "1.11.0"
        const val appcompat = "1.6.1"
        const val androidxCore = "1.12.0"
        const val androidxActivity = "1.8.2"
        const val androidxFragment = "1.6.2"
        const val constraintLayout = "2.1.4"
        const val recyclerview = "1.3.2"
        const val lifecycle = "2.6.2"
        const val annotation = "1.7.1"
        const val swiperefreshlayout = "1.1.0"
        const val room = "2.6.1"
        const val glide = "4.16.0"
        const val camerax = "1.4.0-alpha04"
        const val gson = "2.10.1"

        const val firebase_bom = "32.1.0"
        const val firebase_ui_storage = "8.0.2"
        const val firebase_crashlytics = "2.9.9"

        const val asset_delivery = "2.2.1"
        const val play_delivery = "2.1.0"
        const val play_review = "2.0.1"
        const val play_app_update = "2.1.0"

        const val coroutines_core = "1.8.0"
        const val elasticView = "2.1.0"

        const val navigation = "2.7.7"

        const val preference = "1.2.1"

        const val gms = "4.4.1"

        const val junit = "4.13.2"
        const val test_junit = "1.1.5"
        const val espresso_core = "3.5.1"

        const val kotlinStdlib = "1.9.20"
        const val ktLint = "12.1.0"
    }

    object Libraries {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
        const val gson = "com.google.code.gson:gson:${Versions.gson}"

        const val google_services = "com.google.gms:google-services:${Versions.gms}"

        const val play_asset_delivery = "com.google.android.play:asset-delivery-ktx:${Versions.asset_delivery}"
        const val play_feature_delivery = "com.google.android.play:feature-delivery-ktx:${Versions.play_delivery}"
        const val play_review = "com.google.android.play:review-ktx:${Versions.play_review}"
        const val play_app_update = "com.google.android.play:app-update-ktx:${Versions.play_app_update}"

        const val elastic_view = "com.github.skydoves:elasticviews:${Versions.elasticView}"

        const val preference = "androidx.preference:preference-ktx:${Versions.preference}"
    }

    object Kotlin {
        const val agp = "com.android.tools.build:gradle:${Versions.agp}"
        const val kotlin_gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinStdlib}"
        const val ksp = "com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}"
        const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_core}"
        const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_core}"
        const val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_core}"
        const val ktLint = "org.jlleitschuh.gradle.ktlint:${Versions.ktLint}"
    }

    object Jetpack {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

        const val activity = "androidx.activity:activity-ktx:${Versions.androidxActivity}"
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.androidxFragment}"
        const val androidx_core = "androidx.core:core-ktx:${Versions.androidxCore}"

        const val annotation = "androidx.annotation:annotation:${Versions.annotation}"

        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"

        const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
        const val lifecycle_viewmodel_savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"

        const val hilt_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val hilt_android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"

        const val camera_core = "androidx.camera:camera-core:${Versions.camerax}"
        const val camera_camera2 = "androidx.camera:camera-camera2:${Versions.camerax}"
        const val camera_lifecycle = "androidx.camera:camera-lifecycle:${Versions.camerax}"
        const val camera_video = "androidx.camera:camera-video:${Versions.camerax}"
        const val camera_view = "androidx.camera:camera-view:${Versions.camerax}"
        const val camera_extensions = "androidx.camera:camera-extensions:${Versions.camerax}"

        const val navigation_safeargsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val navigation_dynamic_features_fragment = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
        const val navigation_testing = "androidx.navigation:navigation-testing:${Versions.navigation}"

        const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
        const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
        const val room = "androidx.room:room-ktx:${Versions.room}"
        const val room_testing = "androidx.room:room-testing:${Versions.room}"
        const val room_paging = "androidx.room:room-paging:${Versions.room}"
    }

    object Firebase {
        const val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase_bom}"
        const val firebase_ui_storage = "com.firebaseui:firebase-ui-storage:${Versions.firebase_ui_storage}"

        const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics"
        const val firebase_fireStore = "com.google.firebase:firebase-firestore-ktx"
        const val firebase_analytics = "com.google.firebase:firebase-analytics-ktx"
        const val firebase_auth = "com.google.firebase:firebase-auth-ktx"
        const val firebase_storage = "com.google.firebase:firebase-storage-ktx"
        const val firebase_messaging = "com.google.firebase:firebase-messaging-ktx"
        const val firebase_crashlytics_gradle = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebase_crashlytics}"
    }

    object Test {
        const val ANDROID_JUNIT_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        const val junit = "junit:junit:${Versions.junit}"
        const val test_junit = "androidx.test.ext:junit:${Versions.test_junit}"
        const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    }
}