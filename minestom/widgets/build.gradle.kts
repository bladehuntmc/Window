java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:1_20_5-d911dab9dd")
    compileOnly(project(":minestom"))
}
