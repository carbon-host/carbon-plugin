plugins {
    id("net.kyori.blossom") version "2.1.0"
}

dependencies {
    compileOnly(libs.velocity) {
        // Requires Java 17
        exclude("com.velocitypowered", "velocity-brigadier")
    }
    annotationProcessor(libs.velocity)
}

//sourceSets {
//    main {
//        blossom {
//            resources {
//                property("version", project.version.toString())
//            }
//        }
//    }
//}
