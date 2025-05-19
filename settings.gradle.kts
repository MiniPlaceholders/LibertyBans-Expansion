enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LibertyBans-Expansion"

arrayOf("common", "paper", "velocity").forEach {
    include("libertybans-expansion-$it")

    project(":libertybans-expansion-$it").projectDir = file(it)
}
