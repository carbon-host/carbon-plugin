plugins {
    id("host.carbon.gradle.plugin")
    id("net.kyori.blossom") version "2.1.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-releases/")
    maven("https://repo.papermc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly(libs.velocity) {
        // Requires Java 17
        exclude("com.velocitypowered", "velocity-brigadier")
    }
    annotationProcessor(libs.velocity)
}

sourceSets {
    main {
        blossom {
            resources {
                property("version", project.version.toString())
            }
        }
    }
}
