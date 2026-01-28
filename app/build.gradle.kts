plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "1.9.21"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.shop"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.shop"
        minSdk = 29
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.9")


    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    // (если вы используете Hilt + Jetpack ViewModel)
//    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
//    kapt("androidx.hilt:hilt-compiler:1.0.0")

//    // (если вы используете Hilt + WorkManager)
//    implementation("androidx.hilt:hilt-work:1.0.0")
//    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.compose.runtime:runtime:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit Kotlinx Serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // OkHttp (для ContentType)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")


    //Firebase bom
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    //Firebase store
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

    //Firebase storage
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")

    //Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")


    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Retrofit 2
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.6.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.2")

    //coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    //Live data
    implementation("androidx.compose.runtime:runtime-livedata:1.0.0-beta01")

    //work-manager
    implementation("androidx.work:work-runtime-ktx:2.7.1")




    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")

    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-auth:23.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
hilt {
    enableAggregatingTask = true
}
kapt {
    correctErrorTypes = true
}