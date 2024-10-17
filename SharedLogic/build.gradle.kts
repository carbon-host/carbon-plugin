plugins {
    id("com.github.mlgpenguin.kotlin")
    id("io.ktor.plugin") version "3.0.0"
}

group = "com.github.mlgpenguin"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // IF the downstream modules (spigot/velocity/bungee) need access to KTOR dependency,
    // change 'implementation' to 'api' otherwise abstract methods.

    implementation("io.ktor:ktor-server-core:3.0.0")
    implementation("io.ktor:ktor-server-netty:3.0.0")
}

application {
    mainClass.set("com.github.mlgpenguin.multiplatformtemplate.logic.KtorManager")
}