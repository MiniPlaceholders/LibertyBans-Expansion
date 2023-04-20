repositories {
    maven("https://mvn-repo.arim.space/gpl3/")
    maven("https://mvn-repo.arim.space/lesser-gpl3/")
    maven("https://mvn-repo.arim.space/affero-gpl3/")
}

dependencies {
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.libertybans)
    compileOnly(libs.adventure.api)
    compileOnly(libs.adventure.minimessage)
}