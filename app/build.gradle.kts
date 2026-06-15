plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.pogopvp.overlay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pogopvp.overlay"
        minSdk = 26
        targetSdk = 34
        versionCode = 16
        versionName = "0.1.4"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    // On-device OCR — bundled model, no network needed.
    implementation("com.google.mlkit:text-recognition:16.0.1")

    // Crash reporting → Firebase console (BoM keeps versions aligned).
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
}
