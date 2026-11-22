pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform") version (providers.gradleProperty("kotlinVersion").orNull ?: "2.4.21")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "kotlin-native-regex-stale-captures"
