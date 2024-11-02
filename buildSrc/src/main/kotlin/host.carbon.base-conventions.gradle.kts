plugins {
    `java-library`
    kotlin("jvm")
}

val targetJavaVersionNumber = 21
val targetJavaVersion = JavaVersion.toVersion(targetJavaVersionNumber)

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

//java {
//    sourceCompatibility = targetJavaVersion
//    targetCompatibility = targetJavaVersion
//
//    if (JavaVersion.current() < targetJavaVersion) {
//        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersionNumber))
//    }
//}

//tasks.withType<JavaCompile>().configureEach {
//    options.encoding = "UTF-8"
//
//    if (targetJavaVersionNumber >= 10 || JavaVersion.current().isJava10Compatible) {
//        options.release.set(targetJavaVersionNumber)
//    }
//}
//
