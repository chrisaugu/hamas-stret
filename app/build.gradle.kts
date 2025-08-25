// Project versions are managed in root/version.properties

import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")//or 'com.google.devtools.ksp'
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

fun getIncrementedVersionCode(buildVariant: String): Int {
    val versionFile = file("${project.rootProject.projectDir}/version.properties")
    val versionProps = Properties().apply {
        load(FileInputStream(versionFile))
    }

    val codeKey = "VERSION_CODE_${buildVariant.uppercase()}"
    val baseCode = (versionProps["VERSION_CODE_BASE"] as String).toInt()
    val variantCode = (versionProps.getProperty(codeKey, "0")).toInt() + 1

    versionProps[codeKey] = variantCode.toString()
    versionProps.store(versionFile.writer(), null)

    return baseCode + variantCode
}

//fun getGitVersionCode(): Int {
//    val process = Runtime.getRuntime().exec("git rev-list --count HEAD")
//    process.waitFor()
//    return process.inputStream.bufferedReader().readText().trim().toInt()
//}

fun getAutoIncrementVersionCode(): Int {
    val versionFile = file("${project.rootProject.projectDir}/version.properties")
    if (!versionFile.canRead()) {
        throw GradleException("Could not read version.properties!")
    }

    val versionProps = Properties().apply {
        load(FileInputStream(versionFile))
    }

    val code = (versionProps["VERSION_CODE"] as String).toInt() + 1
    versionProps["VERSION_CODE"] = code.toString()
    versionProps.store(versionFile.writer(), null)

    return code
}

fun getVersionCode(): Int {
    val versionPropsFile = file("${project.rootProject.projectDir}/version.properties")
    val versionProps = Properties().apply {
        load(FileInputStream(versionPropsFile))
    }

    val major = (versionProps["MAJOR_VERSION"] as String).toInt()
    val minor = (versionProps["MINOR_VERSION"] as String).toInt()
    val patch = (versionProps["PATCH_VERSION"] as String).toInt()

    // Format: MMmmpp (Major, Minor, Patch)
    return major * 10000 + minor * 100 + patch
}

fun getVersionName(): String {
    val versionPropsFile = file("${project.rootProject.projectDir}/version.properties")
    val versionProps = Properties().apply {
        load(FileInputStream(versionPropsFile))
    }

    return "${versionProps["MAJOR_VERSION"]}.${versionProps["MINOR_VERSION"]}.${versionProps["PATCH_VERSION"]}"
}

fun getCiVersionCode(): Int {
    return System.getenv("BUILD_NUMBER")?.toInt() ?: 1
}

android {
//    flavorDimensions += "environment"
//    productFlavors {
//        create("dev") {
//            versionCode = getIncrementedVersionCode("dev")
//        }
//        create("prod") {
//            versionCode = getIncrementedVersionCode("prod")
//        }
//    }

    namespace = "io.fantastix.hamasstret"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.fantastix.hamasstret"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        versionCode = getVersionCode()
        versionName = getVersionName()

//        buildConfigField("String", "API_KEY", "\"${System.getenv("api.key")}\"")
//        Properties properties = new Properties()
//        properties.load(project.rootProject.file('local.properties').newDataInputStream())
//        buildConfigField "String", "API_KEY", "\"${properties.getProperty('API_KEY')}\""

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    // Add this block at the end of your build.gradle file
//    signingConfigs {
//        release {
//            storeFile = file('your_keystore_file.jks')
//            storePassword = 'your_keystore_password'
//            keyAlias = 'your_key_alias'
//            keyPassword = 'your_key_password'
//            storePassword = 'your_keystore_password'
//            buildConfigField("String", "API_KEY", API_KEY)
//        }
//    }
    buildTypes {
        release {
//            signingConfig = signingConfigs.release
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.appwrite)

    // Play Services
    implementation(libs.play.services.location)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
//    implementation("com.mapbox.maps:android-ndk27:11.13.4")
    implementation(libs.maps.compose)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.security.crypto)
}

secrets {
    defaultPropertiesFileName ="local.properties"
    propertiesFileName = "secrets.properties"
}