dependencies {
    api("dev.hollowcube:minestom-ce:1554487748")
    //api("net.bladehunt:ReaKt:8b0ade6")
    api(project(":core"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}