
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

//    ext {
//        kodeinDiGenericJvmVersion = "7.3.1"
//    }
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {url= uri("https://jitpack.io")}
        maven { url=uri("https://dl.bintray.com/kodein-framework/Kodein-DI") }
    }
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.google.gms:google-services:4.4.1")
        val navVersion = "2.8.0-alpha03"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

plugins {
    id("com.android.application") version "8.3.0" apply false
    id ("com.android.library") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}