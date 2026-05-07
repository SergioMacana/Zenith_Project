plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.example.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.coroutines.android)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.core)

    api(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)

    ksp(libs.room.compiler)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}
