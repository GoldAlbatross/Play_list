plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp { arg("room.schemaLocation", "$projectDir/schemas") }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    java { toolchain.languageVersion.set(JavaLanguageVersion.of(17)) }
    kotlinOptions { jvmTarget = "17" }

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(libs.bundles.navigation.component)
    implementation(libs.bundles.serialization)
    implementation(libs.coroutines.android)
    implementation(libs.bundles.retrofit2)
    implementation(libs.bundles.room)
    //implementation(libs.dagger)
    implementation(libs.glide)
    implementation(libs.koin)
    //ksp(libs.dagger.compiler)
    ksp(libs.glide.compiler)
    ksp(libs.room.compiler)

    // LeakCanary
    //debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.8.1'
}