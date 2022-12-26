import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {

    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    //firebase
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
    //Manifest 보안
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    //hilt
    id("dagger.hilt.android.plugin")
    //serialization
    id("kotlinx-serialization")
}

android {
    namespace = Versions.APPLICATION_ID
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = Versions.APPLICATION_ID
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.ANDROID_VERSION_CODE
        versionName = Versions.ANDROID_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "KAKAO_APP_KEY", getProperty("kakao_app_key"))
        //manifest에서 사용할 경우
        manifestPlaceholders["KAKAO_OAUTH_KEY"] = getProperty("kakao_oauth_key")

    }
    signingConfigs {
        create("release") {
            keyAlias = getProperty("RELEASE_KEY_ALIAS")
            keyPassword = getProperty("RELEASE_KEY_PASSWORD")
            storePassword = getProperty("RELEASE_STORE_PASSWORD")
            storeFile = file(getProperty("RELEASE_STORE_FILE"))
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //디버그 테스트할 때  사용
            isDebuggable = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(Ktx.CORE)
    implementation(AndroidX.APP_COMPAT)
    implementation(Google.MATERIAL)
    testImplementation(Test.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
    implementation(Google.PLAY_CORE)
    implementation(Legacy.LEGACY_SUPPORT)// 인앱 업데이트 라이브러리

    //Firebase
    implementation(platform(Firebase.FIREBASE_BOM))
    implementation(Firebase.FIREBASE_MESSAGING)
    implementation(Firebase.FIREBASE_ANALYTICS)
    implementation(Firebase.FIREBASE_CRASHLYTICS)

    //EncryptedSharedPreferences
    implementation(EncryptedSharedPreferences.SECURITY_CRYPTO)

    //Retrofit2
    implementation(Retrofit.RETROFIT)
    implementation(Retrofit.CONVERTER_SERIALIZATION)
    //okhttp3
    implementation(OkHttp.OKHTTP)
    implementation(OkHttp.LOGGING_INTERCEPTOR)

    //Serialization
    implementation(Serialization.SERIALIZATION)

    //kakao login
    implementation(Kakao.KAKAO)

    //Hilt
    implementation(Hilt.HILT_ANDROID)
    kapt(Hilt.HILT_ANDROID_COMPILER)

    // Timber
    implementation(Timber.TIMBER)

    //Room
    implementation(Room.ROOM_KTX)
    implementation(Room.ROOM_RUNTIME)
    kapt(Room.ROOM_COMPILER)
}