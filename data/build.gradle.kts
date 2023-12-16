plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.movingmaker.data"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(Ktx.CORE)
    implementation(AndroidX.APP_COMPAT)
    implementation(Google.MATERIAL)
    testImplementation(Test.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)

    //Retrofit2
    implementation(Retrofit.RETROFIT)
    implementation(Retrofit.CONVERTER_SERIALIZATION)

    //okhttp3
    implementation(OkHttp.OKHTTP)
    implementation(OkHttp.LOGGING_INTERCEPTOR)

    //Serialization
    implementation(Serialization.SERIALIZATION)

    //Hilt
    implementation(Hilt.HILT_ANDROID)
    kapt(Hilt.HILT_ANDROID_COMPILER)

    // Timber
    implementation(Timber.TIMBER)

    //Room
    implementation(Room.ROOM_KTX)
    implementation(Room.ROOM_RUNTIME)
    kapt(Room.ROOM_COMPILER)

    // Preferences DataStore (SharedPreferences like APIs)
    implementation(DataStore.DATASTORE_PREFERENCES)
}