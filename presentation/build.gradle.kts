import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
//    //navigation
    id("androidx.navigation.safeargs.kotlin")
//    //Manifest 보안
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//    //hilt
    id("dagger.hilt.android.plugin")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.movingmaker.presentation"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "KAKAO_APP_KEY", getProperty("kakao_app_key"))

        //manifest에서 사용할 경우
        manifestPlaceholders["KAKAO_OAUTH_KEY"] = getProperty("kakao_oauth_key")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

fun getProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation(project(":domain"))

    implementation(AndroidX.CONSTRAINT_LAYOUT)
    implementation(Ktx.CORE)
    implementation(AndroidX.APP_COMPAT)
    implementation(Google.MATERIAL)
    testImplementation(Test.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
    implementation(Google.PLAY_CORE)
    implementation(Legacy.LEGACY_SUPPORT)// 인앱 업데이트 라이브러리
    implementation(AndroidX.RECYCLERVIEW)

    //ktx
    implementation(Ktx.ACTIVITY)
    implementation(Ktx.FRAGMENT)

    //viewpager2
    implementation(ViewPager2.VIEW_PAGER2)
    //indicator for viewpager2
    implementation(Indicator.INDICATOR)


    //lifecycle
    implementation(Lifecycle.LIFECYCLE)
    // ViewModel
    implementation(Lifecycle.LIFECYCLE_VIEWMODEL_KTX)
    // LiveData
    implementation(Lifecycle.LIFECYCLE_LIVEDATA_KTX)

    //material-calendarview
    implementation(MaterialCalendar.MATERIAL_CALENDAR_VIEW)

    //Firebase
    implementation(platform(Firebase.FIREBASE_BOM))
    implementation(Firebase.FIREBASE_MESSAGING)
    implementation(Firebase.FIREBASE_ANALYTICS)
    implementation(Firebase.FIREBASE_CRASHLYTICS)
    implementation(Firebase.FIREBASE_AUTHENTICATION)
    implementation(Firebase.FIREBASE_UI_AUTH)
    implementation(Firebase.FIREBASE_FIRESTORE)

    //Google
    implementation(Google.GOOGLE_PLAY_SERVICES_AUTH)


    //Navigation
    implementation(Navigation.NAVIGATION_UI)
    implementation(Navigation.NAVIGATION_FRAGMENT)
    implementation(Navigation.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT)

    //EncryptedSharedPreferences
    implementation(EncryptedSharedPreferences.SECURITY_CRYPTO)

    //kakao login
    implementation(Kakao.KAKAO)

    //Hilt
    implementation(Hilt.HILT_ANDROID)
    kapt(Hilt.HILT_ANDROID_COMPILER)

    //Serialization
    implementation(Serialization.SERIALIZATION)

    // Timber
    implementation(Timber.TIMBER)

    // Preferences DataStore (SharedPreferences like APIs)
    implementation(DataStore.DATASTORE_PREFERENCES)

}