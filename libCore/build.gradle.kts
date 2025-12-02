import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.gradlePlugin)
    alias(libs.plugins.jacoco)
}

android {
    namespace = "com.mindjourney.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 29

        val properties = Properties()
        val localProperties = rootProject.file("local.properties")
        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
        }
        val developerEmail = properties.getProperty("developer.email") ?: "not_set"
        buildConfigField(
            "String",
            "DEVELOPER_EMAIL",
            "\"$developerEmail\""
        )
        buildConfigField("Integer", "POLL_CYCLES", "1")
        buildConfigField("Integer", "POLL_INTERVAL_SEC", "10")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        jvmToolchain(21)
    }

     // 🔥 it is necessary to have it
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.jakarta.inject)
    implementation(libs.ksp.symbol.processing)
    implementation(libs.review.ktx)
    implementation(libs.kotlinpoet)
    implementation(libs.javapoet)

    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)
    ksp(libs.hilt.compiler)


    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlin.reflect)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.cyeksan.onboarding)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.junit5.jupiter.api)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test.coroutines)
    testImplementation(libs.cash.turbine)
}
