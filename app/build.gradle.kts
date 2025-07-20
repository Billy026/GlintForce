plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.billy.glintforce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.billy.glintforce"
        minSdk = 26
        resConfigs("en", "zh-rCN", "ms", "in-rID", "ja", "ko", "th", "vi")
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.ui.viewbinding)
    implementation(libs.material)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Widget
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.glance.appwidget)

    // Vico Charts
    implementation(libs.compose)
    implementation(libs.compose.m2)
    implementation(libs.compose.m3)
    implementation(libs.core)
    implementation(libs.views)

    // Firebase database
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)

    // Authentication
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}