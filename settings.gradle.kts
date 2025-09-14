enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "LibertyBans-Expansion"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://mvn-repo.arim.space/lesser-gpl3/")
        maven("https://mvn-repo.arim.space/affero-gpl3/")
    }
}

arrayOf("common", "paper", "velocity", "sponge").forEach {
    include("libertybans-expansion-$it")

    project(":libertybans-expansion-$it").projectDir = file(it)
}
