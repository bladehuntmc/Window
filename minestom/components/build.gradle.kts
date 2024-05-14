java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:1_20_5-323c75f8a5")
    compileOnly(project(":minestom"))
}