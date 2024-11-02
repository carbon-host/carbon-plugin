plugins {
    id("host.carbon.gradle.kotlin")
    id("com.github.johnrengelman.shadow")
}

allprojects {
    group = "host.carbon"
    version = property("projectVersion") as String
    description = "The Plugin providing information to Carbon.host"
}

dependencies {
    implementation(project(":spigot"))
    implementation(project(":velocity"))
    implementation(project(":bungee"))
}

tasks {
     jar {
        enabled = false
     }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        destinationDirectory.set(file("$rootDir/build"))
    }

    clean {
        dependsOn(
            project(":velocity").tasks.clean,
            project(":bungee").tasks.clean,
            project(":spigot").tasks.clean,
            project(":common").tasks.clean,
        )
    }
}

