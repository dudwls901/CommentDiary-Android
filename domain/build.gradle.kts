plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    //hilt
    implementation(Hilt.HILT_CORE)

    //coroutines
    implementation(Coroutines.COROUTINES_CORE)

    //Serialization
    implementation(Serialization.SERIALIZATION)
}