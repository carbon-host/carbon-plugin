plugins {
    `java-library`
    kotlin("jvm")
}

val targetJavaVersionNumber = 21

tasks {
    // Variable replacements
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            println(project.version)
            println("VERSION: ${project.version}")
            expand(
                "version" to project.version,
                "description" to project.description,
                "website" to "https://carbon.host",
            )
        }
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.addAll(listOf("-nowarn", "-Xlint:-unchecked", "-Xlint:-deprecation"))
    }
}

kotlin {
    jvmToolchain(targetJavaVersionNumber)
}

java {
    javaTarget(21)
    withSourcesJar()
}