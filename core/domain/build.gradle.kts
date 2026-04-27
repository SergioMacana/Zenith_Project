plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.coroutines.android)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}