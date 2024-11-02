plugins {
    base
    id("host.carbon.build-logic")
}

allprojects {
    group = "host.carbon"
    version = property("projectVersion") as String
    description = "The Plugin providing information to Carbon.host"
}


val main = setOf(
    projects.carbon,
    projects.carbonCommon,
    projects.carbonSpigot,
    projects.carbonBungee,
    projects.carbonVelocity,
).map { it.dependencyProject }

subprojects {
    when (this) {
        in main -> plugins.apply("host.carbon.shadow-conventions")
        else -> plugins.apply("host.carbon.base-conventions")
    }
}
