plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    // SOLO Kotlin puro (nada más)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}