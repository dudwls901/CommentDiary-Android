object AndroidX {
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
}

object Ktx {
    const val CORE = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"
}

object Google {
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val PLAY_CORE = "com.google.android.play:core-ktx:${Versions.PLAY_CORE}"
    const val GOOGLE_PLAY_SERVICES_AUTH = "com.google.android.gms:play-services-auth:${Versions.GOOGLE_PLAY_SERIVCE_AUTH}"
}

object Test {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object AndroidTest {
    const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}

object Legacy {
    const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT_V4}"
}

object Hilt {
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val HILT_CORE = "com.google.dagger:hilt-core:${Versions.HILT}"
}

object Retrofit {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val CONVERTER_SERIALIZATION =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT_SERIALIZATION_CONVERTER}"
}

object OkHttp {
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
}

object ViewPager2 {
    const val VIEW_PAGER2 = "androidx.viewpager2:viewpager2:${Versions.VIEW_PAGER2}"
}

object Indicator {
    const val INDICATOR = "me.relex:circleindicator:${Versions.CIRCLE_INDICATOR}"
}

object MaterialCalendar {
    const val MATERIAL_CALENDAR_VIEW = "com.prolificinteractive:material-calendarview:${Versions.MATERIAL_CALENDAR_VIEW}"
}

object Room {
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
}

object Coroutines {
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}"
}

object Lifecycle {
    const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
}

object Navigation {
    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_DYNAMIC_FEATURES_FRAGMENT = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.NAVIGATION}"
}

object EncryptedSharedPreferences {
    const val SECURITY_CRYPTO = "androidx.security:security-crypto:${Versions.SECURITY_CRYPTO}"
}

object Timber {
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
}

object Serialization {
    const val SERIALIZATION =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.SERIALIZATION}"
}

object Firebase {
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
    const val FIREBASE_MESSAGING = "com.google.firebase:firebase-messaging-ktx"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
    const val FIREBASE_AUTHENTICATION = "com.google.firebase:firebase-auth-ktx"
    const val FIREBASE_UI_AUTH = "com.firebaseui:firebase-ui-auth:${Versions.FIREBASE_UI_AUTH}"
    const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
}

object Kakao {
    const val KAKAO = "com.kakao.sdk:v2-user:${Versions.KAKAO}"
}

object DataStore {
    const val DATASTORE_PREFERENCES = "androidx.datastore:datastore-preferences:${Versions.DATASTORE_PREFERENCES}"
    const val DATASTORE_PROTO = "androidx.datastore:datastore:${Versions.DATASTORE_PROTO}"

    // without an Android dependency
    const val DATASTORE_PREFERENCES_CORE = "androidx.datastore:datastore-preferences-core:${Versions.DATASTORE_PREFERENCES}"
    const val DATASTORE_PROTO_CORE = "androidx.datastore:datastore-core:${Versions.DATASTORE_PROTO}"
}