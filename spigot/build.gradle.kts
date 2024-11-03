dependencies {
    compileOnly(projects.carbonCommon)
    compileOnly(libs.spark)
    compileOnly(libs.paper) {
        exclude("junit", "junit")
        exclude("com.google.code.gson", "gson")
        exclude("javax.persistence", "persistence-api")
    }
}