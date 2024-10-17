@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(modulePublication)
}

android {
    namespace = "icu.ketal.katexmath"
    compileSdk = libs.versions.compileSdk.get().toInt()
    ndkVersion = "27.1.12297006"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=none"
                val flags = arrayOf(
                    "-fno-rtti",
                    "-fno-exceptions",
                    "-fvisibility=protected",
                    "-fvisibility-inlines-hidden",
                    "-ffunction-sections",
                    "-fdata-sections",
                    "-fmerge-all-constants",
                    "-Oz"
                )
                cppFlags(*flags)
                cFlags(*flags)
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

    buildFeatures {
        prefab = true
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

dependencies {
    implementation("dev.rikka.ndk.thirdparty:cxx:1.2.0")
}
