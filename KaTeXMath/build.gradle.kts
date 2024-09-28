@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "icu.ketal.katexmath"
    compileSdk = libs.versions.compileSdk.get().toInt()
    ndkVersion = "27.1.12297006"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=c++_shared"
                targets += "katexmath"
            }
        }
    }
    lint.targetSdk = libs.versions.targetSdk.get().toInt()

    externalNativeBuild {
        cmake {
            path = File(projectDir, "src/main/cpp/CMakeLists.txt")
            version = "3.30.3"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
