rootProject.name = "norris"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("gradle/plugins")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

include(
    // Product
    ":app",

    // JVM platform
    ":platform:jvm:core-rest",
    ":platform:jvm:testing-helpers",
    ":platform:jvm:testing-mockserver",
    ":platform:jvm:testing-rest",

    // Android platform
    ":platform:android:core-assets",
    ":platform:android:core-navigator",
    ":platform:android:core-persistence",
    ":platform:android:testing-application",
    ":platform:android:testing-helpers",
    ":platform:android:testing-persistence",
    ":platform:android:testing-screenshots",

    // Features
    ":features:facts",
    ":features:search"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
