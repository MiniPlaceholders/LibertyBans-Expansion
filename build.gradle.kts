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

allprojects {
    apply<JavaPlugin>()
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
    }
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
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    build {
        dependsOn(shadowJar)
    }
}
