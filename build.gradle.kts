plugins {
    java
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.libertybansExpansionCommon)
    implementation(projects.libertybansExpansionVelocity)
    implementation(projects.libertybansExpansionPaper)
    compileOnly(libs.miniplaceholders)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

subprojects {
    apply<JavaPlugin>()
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(21)
        }
    }
}

tasks {
    shadowJar {
        archiveFileName.set("LibertyBans-Expansion-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    build {
        dependsOn(shadowJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
}
