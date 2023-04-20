plugins {
    java
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.libertybansExpansionVelocity)
    implementation(projects.libertybansExpansionPaper)
}

subprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
    }
    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
    }
}

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    build {
        dependsOn(shadowJar)
    }
}
