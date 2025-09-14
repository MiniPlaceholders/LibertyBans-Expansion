plugins {
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

repositories {
    maven("https://mvn-repo.arim.space/lesser-gpl3/")
    maven("https://mvn-repo.arim.space/affero-gpl3/")
}

dependencies {
    compileOnly("space.arim.libertybans:bans-api:1.1.0")
    compileOnly(libs.miniplaceholders)

    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
    implementation(libs.caffeine)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}
