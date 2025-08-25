// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
//    kotlin("kapt")                      // for annotation processing
//    id("dagger.hilt.android.plugin")   // Hilt plugin
    alias(libs.plugins.dagger) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
}