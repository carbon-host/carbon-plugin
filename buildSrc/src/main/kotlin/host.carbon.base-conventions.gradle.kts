plugins {
    kotlin("jvm")
}

val targetJavaVersionNumber = 21
val targetJavaVersion = JavaVersion.toVersion(targetJavaVersionNumber)

tasks {
    // Variable replacements
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand(
                "version" to project.version,
                "description" to project.description,
                "website" to "https://carbon.host",
            )
        }
    }
}

kotlin {
    jvmToolchain(targetJavaVersionNumber)
}

java {
    sourceCompatibility = targetJavaVersion
    targetCompatibility = targetJavaVersion

    if (JavaVersion.current() < targetJavaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersionNumber))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    if (targetJavaVersionNumber >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersionNumber)
    }
}