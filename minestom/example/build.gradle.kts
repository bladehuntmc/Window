java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("net.minestom:minestom-snapshots:1_20_5-d911dab9dd")
    implementation(project(":minestom"))
    implementation(project(":minestom:widgets"))
}
