plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.sensorprint"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sensorprint"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    signingConfigs {
//        release {
//            storeFile System.getenv("SIGNING_KEY_STORE")
//            storePassword System.getenv("SIGNING_STORE_PASSWORD")
//            keyAlias System.getenv("SIGNING_KEY_ALIAS")
//            keyPassword System.getenv("SIGNING_KEY_PASSWORD")
//        }
//    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
