import dependencies.Dep
import dependencies.Packages
import dependencies.Versions

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.androidCompileSdkVersion)
    defaultConfig {
        applicationId = Packages.name
        minSdkVersion(Versions.androidMinSdkVersion)
        targetSdkVersion(Versions.androidTargetSdkVersion)
        versionCode = Versions.androidVersionCode
        versionName = Versions.androidVersionName
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = Packages.debugNameSuffix
            buildConfigField("String", "TMDB_API_KEY", "\"25d3da3734055bccdaf9e2c587d94271\"")
        }

        getByName("release") {
            buildConfigField("String", "TMDB_API_KEY", "\"25d3da3734055bccdaf9e2c587d94271\"")
        }

    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
        exclude("META-INF/proguard/*.pro")
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":corecomponent:androidcomponets"))

    implementation(project(":feature:account"))
    implementation(project(":feature:movies"))
    implementation(project(":feature:system"))
    implementation(project(":feature:authentication"))

    implementation(project(":data:api"))
    implementation(project(":data:db"))
    implementation(project(":data:model"))
    implementation(project(":data:repository"))

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.AndroidX.appCompat)
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.constraint)

    implementation(Dep.Dagger.core)
    implementation(Dep.Dagger.androidSupport)
    implementation(Dep.Dagger.android)
    kapt(Dep.Dagger.compiler)
    kapt(Dep.Dagger.androidProcessor)
    compileOnly(Dep.Dagger.assistedInjectAnnotations)
    kapt(Dep.Dagger.assistedInjectProcessor)

    debugImplementation(Dep.LeakCanary.leakCanary)

}