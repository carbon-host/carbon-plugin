enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    // configures repositories for all projects
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenCentral()
    }
    // only use these repos
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    // default plugin versions
    plugins {
        id("net.kyori.blossom") version "2.1.0"
        id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
        id("com.gradleup.shadow") version "8.3.0"
    }
}

rootProject.name = "carbon-parent"

setupCarbonSubproject("common")
setupCarbonSubproject("spigot")
setupCarbonSubproject("bungee")
setupCarbonSubproject("velocity")

setupSubproject("carbon") {
    projectDir = file("universal")
}

fun setupCarbonSubproject(name: String) {
    setupSubproject("carbon-$name") {
        projectDir = file(name)
    }
}

inline fun setupSubproject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}