
plugins {
    id("com.android.application") version "8.9.0" apply false
    id("com.android.library") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id ("com.google.dagger.hilt.android") version "2.51.1" apply false
    id ("org.jetbrains.kotlin.kapt") version "1.9.21"



}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")

    }
}

