dependencies {
    api("net.minestom:minestom-snapshots:7320437640")
    //api("net.bladehunt:ReaKt:8b0ade6")
    api(project(":core"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}