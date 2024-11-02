plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta2")
    implementation("com.gradleup.shadow", "shadow-gradle-plugin", "8.3.0")
}