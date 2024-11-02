plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "carbon-plugin"

include("spigot", "bungee", "velocity", "common")
