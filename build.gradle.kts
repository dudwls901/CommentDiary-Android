// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_VERSION}")
        //navigation
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")

        //firebase
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS}")
        //Manifest 보안
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.MANIFEST_SECRET}")
        //hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        //serialization
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN_VERSION}")
    }
}

plugins {
    id("com.android.application") version Versions.GRADLE_VERSION apply false
    id("com.android.library") version Versions.GRADLE_VERSION apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN_VERSION apply false
}

//task clean (type: Delete) {
//    delete rootProject . buildDir
//}