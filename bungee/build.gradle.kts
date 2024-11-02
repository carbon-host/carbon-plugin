plugins {
    id("host.carbon.gradle.plugin")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly(libs.bungee)
}

configureProcessResourcesFor("bungee.yml")


