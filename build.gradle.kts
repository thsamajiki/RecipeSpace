// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Kotlin.agp)
        classpath(Dependencies.Kotlin.kotlin_gradlePlugin)
        classpath(Dependencies.Jetpack.hilt_plugin)
        classpath(Dependencies.Libraries.google_services)
        classpath(Dependencies.Jetpack.navigation_safeargsPlugin)
        classpath(Dependencies.Firebase.firebase_crashlytics_gradle)
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
