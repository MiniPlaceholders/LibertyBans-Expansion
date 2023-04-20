plugins {
    alias(libs.plugins.blossom)
}

dependencies {
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
    compileOnly(libs.miniplaceholders)
    compileOnly(projects.libertybansExpansionCommon)
}

blossom {
    replaceTokenIn("src/main/java/io/github/miniplaceholders/expansion/example/velocity/Constants.java")
    replaceToken("{version}", project.version)
}
