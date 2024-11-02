import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar

plugins {
    id("host.carbon.base-conventions")
    id("maven-publish")
    id("com.gradleup.shadow")
}

tasks {
    named<Jar>("jar") {
        archiveClassifier.set("unshaded")
        from(project.rootProject.file("LICENSE"))
    }
    val shadowJar = named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        configureRelocations()
        configureExcludes()
    }
    named("build") {
        dependsOn(shadowJar)
    }
}

fun ShadowJar.configureRelocations() {
    relocate("com.google.gson", "host.carbon.plugin.libs.gson")
    relocate("org.yaml.snakeyaml", "host.carbon.plugin.libs.snakeyaml")
}

fun ShadowJar.configureExcludes() {
    // FastUtil - we only want object, int, and certain reference maps
    // Object types
    exclude("it/unimi/dsi/fastutil/*/*2Reference*")
    exclude("it/unimi/dsi/fastutil/*/*Reference2Int*")
    exclude("it/unimi/dsi/fastutil/*/*Boolean*")
    exclude("it/unimi/dsi/fastutil/*/*Byte*")
    exclude("it/unimi/dsi/fastutil/*/*Short*")
    exclude("it/unimi/dsi/fastutil/*/*Float*")
    exclude("it/unimi/dsi/fastutil/*/*Double*")
    exclude("it/unimi/dsi/fastutil/*/*Long*")
    exclude("it/unimi/dsi/fastutil/*/*Char*")
    // Map types
    exclude("it/unimi/dsi/fastutil/*/*Custom*")
    exclude("it/unimi/dsi/fastutil/*/*Tree*")
    exclude("it/unimi/dsi/fastutil/*/*Heap*")
    exclude("it/unimi/dsi/fastutil/*/*Queue*")
    // Crossing fingers
    exclude("it/unimi/dsi/fastutil/*/*Big*")
    exclude("it/unimi/dsi/fastutil/*/*Synchronized*")
    exclude("it/unimi/dsi/fastutil/*/*Unmodifiable*")
    // More
    exclude("it/unimi/dsi/fastutil/io/TextIO")
}