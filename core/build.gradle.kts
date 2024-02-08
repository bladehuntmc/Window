dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("net.bladehunt:ReaKt:8b0ade6")
}

tasks.test {
    useJUnitPlatform()
}