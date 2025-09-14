plugins {
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

dependencies {
    compileOnly(libs.libertybans)
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
