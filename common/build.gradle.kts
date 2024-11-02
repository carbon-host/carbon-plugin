plugins {
    id("io.ktor.plugin") version "3.0.0"
}

dependencies {
    // IF the downstream modules (spigot/velocity/bungee) need access to KTOR dependency,
    // change 'implementation' to 'api' otherwise abstract methods.

    implementation("io.ktor:ktor-server-core:3.0.0")
    implementation("io.ktor:ktor-server-netty:3.0.0")
}

application {
    mainClass.set("host.carbon.common.KtorManager")
}